package mx.tec.appbase.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import mx.tec.appbase.navigation.Pantalla
import mx.tec.appbase.navigation.pantallasBarraInferior

// Bottom navigation: una opción por cada pantalla de pantallasBarraInferior.
@Composable
fun BarraInferior(
    rutaActual: String?,
    onSeleccionar: (Pantalla) -> Unit,
) {
    NavigationBar {
        pantallasBarraInferior.forEach { pantalla ->
            NavigationBarItem(
                selected = rutaActual == pantalla.route,
                onClick = { onSeleccionar(pantalla) },
                icon = { Icon(pantalla.icono, contentDescription = pantalla.titulo) },
                label = { Text(pantalla.titulo) },
            )
        }
    }
}
