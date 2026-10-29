package mx.tec.appbase.navigation

import androidx.compose.runtime.Composable
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

    NavHost(navController = navController, startDestination = RutasApp.LOGIN) {

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
            PrincipalScreen(
                sesionViewModel = sesionViewModel,
                onCerrarSesion = {
                    // Borra el token y el usuario de memoria.
                    sesionViewModel.cerrarSesion()
                    navController.navigate(RutasApp.LOGIN) {
                        // Saca la parte principal de la pila: con "atrás" no se regresa a ella.
                        popUpTo(RutasApp.PRINCIPAL) { inclusive = true }
                    }
                }
            )
        }
    }
}
