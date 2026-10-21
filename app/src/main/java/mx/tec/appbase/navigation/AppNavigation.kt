package mx.tec.appbase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.tec.appbase.ui.screens.LoginScreen
import mx.tec.appbase.ui.screens.PrincipalScreen

// Navegación de NIVEL 1: Login <-> Principal
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RutasApp.LOGIN) {

        composable(RutasApp.LOGIN) {
            LoginScreen(
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
                onCerrarSesion = {
                    navController.navigate(RutasApp.LOGIN) {
                        // Saca la parte principal de la pila: con "atrás" no se regresa a ella.
                        popUpTo(RutasApp.PRINCIPAL) { inclusive = true }
                    }
                }
            )
        }
    }
}
