package mx.tec.appbase.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// ------------------------------------------------------------------
// NIVEL 1: rutas de la app completa
// La app tiene solo dos "lugares": el login y la parte principal.
// ------------------------------------------------------------------
object RutasApp {
    const val LOGIN = "login"
    const val PRINCIPAL = "principal"
}

// ------------------------------------------------------------------
// NIVEL 2: pantallas dentro de la parte principal
// Cada pantalla guarda su ruta, el título de la barra superior y su ícono.
// Para agregar una pantalla nueva:
//   1. Agrega aquí un data object.
//   2. Agrégalo a una de las listas de abajo (barra inferior o menú lateral).
//   3. Regístralo en PrincipalNavHost.kt con composable(...).
// ------------------------------------------------------------------
sealed class Pantalla(val route: String, val titulo: String, val icono: ImageVector) {

    // Barra inferior (bottom navigation)
    data object Inicio : Pantalla("inicio", "Inicio", Icons.Filled.Home)
    data object Buscar : Pantalla("buscar", "Buscar", Icons.Filled.Search)
    data object Notificaciones : Pantalla("notificaciones", "Avisos", Icons.Filled.Notifications)
    data object Favoritos : Pantalla("favoritos", "Favoritos", Icons.Filled.Favorite)
    data object Perfil : Pantalla("perfil", "Perfil", Icons.Filled.Person)

    // Menú lateral (navigation drawer)
    data object Calendario : Pantalla("calendario", "Calendario", Icons.Filled.DateRange)
    data object Mensajes : Pantalla("mensajes", "Mensajes", Icons.Filled.Email)
    data object Reportes : Pantalla("reportes", "Reportes", Icons.AutoMirrored.Filled.List)
    data object Configuracion : Pantalla("configuracion", "Configuración", Icons.Filled.Settings)
    data object AcercaDe : Pantalla("acerca_de", "Acerca de", Icons.Filled.Info)
}

// Material Design recomienda de 3 a 5 opciones en la barra inferior.
val pantallasBarraInferior = listOf(
    Pantalla.Inicio,
    Pantalla.Buscar,
    Pantalla.Notificaciones,
    Pantalla.Favoritos,
    Pantalla.Perfil,
)

val pantallasMenuLateral = listOf(
    Pantalla.Calendario,
    Pantalla.Mensajes,
    Pantalla.Reportes,
    Pantalla.Configuracion,
    Pantalla.AcercaDe,
)

// Se usa para encontrar el título de la pantalla actual.
val todasLasPantallas = pantallasBarraInferior + pantallasMenuLateral
