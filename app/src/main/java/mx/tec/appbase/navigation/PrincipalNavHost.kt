package mx.tec.appbase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mx.tec.appbase.ui.screens.inferior.BuscarScreen
import mx.tec.appbase.ui.screens.inferior.FavoritosScreen
import mx.tec.appbase.ui.screens.inferior.InicioScreen
import mx.tec.appbase.ui.screens.inferior.NotificacionesScreen
import mx.tec.appbase.ui.screens.inferior.PerfilScreen
import mx.tec.appbase.ui.screens.lateral.AcercaDeScreen
import mx.tec.appbase.ui.screens.lateral.CalendarioScreen
import mx.tec.appbase.ui.screens.lateral.ConfiguracionScreen
import mx.tec.appbase.ui.screens.lateral.MensajesScreen
import mx.tec.appbase.ui.screens.lateral.ReportesScreen

// Navegación de NIVEL 2: las pantallas que viven dentro de la parte principal.
// Este NavHost se dibuja en medio del Scaffold, entre la barra superior y la inferior.
@Composable
fun PrincipalNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.Inicio.route,
        modifier = modifier,
    ) {
        // Barra inferior
        composable(Pantalla.Inicio.route) { InicioScreen() }
        composable(Pantalla.Buscar.route) { BuscarScreen() }
        composable(Pantalla.Notificaciones.route) { NotificacionesScreen() }
        composable(Pantalla.Favoritos.route) { FavoritosScreen() }
        composable(Pantalla.Perfil.route) { PerfilScreen() }

        // Menú lateral
        composable(Pantalla.Calendario.route) { CalendarioScreen() }
        composable(Pantalla.Mensajes.route) { MensajesScreen() }
        composable(Pantalla.Reportes.route) { ReportesScreen() }
        composable(Pantalla.Configuracion.route) { ConfiguracionScreen() }
        composable(Pantalla.AcercaDe.route) { AcercaDeScreen() }
    }
}
