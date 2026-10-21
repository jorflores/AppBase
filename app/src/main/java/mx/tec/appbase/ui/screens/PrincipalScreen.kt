package mx.tec.appbase.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mx.tec.appbase.navigation.PrincipalNavHost
import mx.tec.appbase.navigation.todasLasPantallas
import mx.tec.appbase.ui.components.BarraInferior
import mx.tec.appbase.ui.components.BarraSuperior
import mx.tec.appbase.ui.components.MenuLateral

// El "cascarón" de la app: menú lateral + barra superior + barra inferior,
// y en medio el NavHost con las pantallas.
@Composable
fun PrincipalScreen(onCerrarSesion: () -> Unit) {
    // NavController propio de la parte principal (distinto al de AppNavigation).
    val navController = rememberNavController()

    // Estado del menú lateral: abierto o cerrado.
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // Abrir y cerrar el menú es una animación, por eso se hace dentro de una corrutina.
    val scope = rememberCoroutineScope()

    // Ruta de la pantalla visible. Se actualiza sola cada vez que navegamos.
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val tituloActual = todasLasPantallas.find { it.route == rutaActual }?.titulo ?: ""

    // Si el menú está abierto, el botón "atrás" lo cierra en lugar de salir.
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuLateral(
                rutaActual = rutaActual,
                onSeleccionar = { pantalla ->
                    scope.launch { drawerState.close() }
                    navController.navigate(pantalla.route) {
                        // Evita apilar la misma pantalla dos veces.
                        launchSingleTop = true
                    }
                },
                onCerrarSesion = {
                    scope.launch { drawerState.close() }
                    onCerrarSesion()
                },
            )
        },
    ) {
        Scaffold(
            topBar = {
                BarraSuperior(
                    titulo = tituloActual,
                    onAbrirMenu = { scope.launch { drawerState.open() } },
                )
            },
            bottomBar = {
                BarraInferior(
                    rutaActual = rutaActual,
                    onSeleccionar = { pantalla ->
                        navController.navigate(pantalla.route) {
                            // Patrón recomendado para barras inferiores:
                            // regresa a Inicio sin acumular pantallas en la pila
                            // y conserva el estado de cada pestaña.
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            },
        ) { innerPadding ->
            PrincipalNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
