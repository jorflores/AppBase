package mx.tec.appbase.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.tec.appbase.ui.screens.LoginScreen
import mx.tec.appbase.ui.screens.PrincipalScreen
import mx.tec.appbase.ui.viewmodel.SesionViewModel

// Navegación de NIVEL 1: Login <-> Principal
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Se crea ARRIBA del NavHost: un solo SesionViewModel para toda la app.
    // Así el token y el usuario siguen disponibles al pasar del login a la parte principal.
    val sesionViewModel: SesionViewModel = viewModel()
    val estado by sesionViewModel.estado.collectAsState()

    // Mientras se lee el token de DataStore (una fracción de segundo), mostramos una carga.
    if (estado.revisandoSesion) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Si había un token guardado, la app abre directo en la parte principal.
        // remember: la pantalla inicial se decide UNA sola vez al abrir la app.
        val pantallaInicial = remember {
            if (estado.token != null) RutasApp.PRINCIPAL else RutasApp.LOGIN
        }

        NavHost(navController = navController, startDestination = pantallaInicial) {

            composable(RutasApp.LOGIN) {
                LoginScreen(
                    sesionViewModel = sesionViewModel,
                    onLoginExitoso = {
                        navController.navigate(RutasApp.PRINCIPAL) {
                            // Saca el login de la pila: con "atrás" no se regresa al login.
                            popUpTo(RutasApp.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(RutasApp.PRINCIPAL) {
                val sesion by sesionViewModel.estado.collectAsState()

                // Si el token desaparece (cerrar sesión o token expirado), regresamos al login.
                LaunchedEffect(sesion.token) {
                    if (sesion.token == null) {
                        navController.navigate(RutasApp.LOGIN) {
                            // Saca la parte principal de la pila: con "atrás" no se regresa a ella.
                            popUpTo(RutasApp.PRINCIPAL) { inclusive = true }
                        }
                    }
                }

                PrincipalScreen(
                    sesionViewModel = sesionViewModel,
                    // Borra el token de memoria y de DataStore; el LaunchedEffect de arriba navega al login.
                    onCerrarSesion = { sesionViewModel.cerrarSesion() }
                )
            }
        }
    }
}
