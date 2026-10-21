package mx.tec.appbase.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.appbase.navigation.Pantalla
import mx.tec.appbase.navigation.pantallasMenuLateral

// Navigation drawer: encabezado, una opción por pantalla y "Cerrar sesión" al final.
@Composable
fun MenuLateral(
    rutaActual: String?,
    onSeleccionar: (Pantalla) -> Unit,
    onCerrarSesion: () -> Unit,
) {
    ModalDrawerSheet {
        // Encabezado: aquí puedes mostrar los datos del usuario que inició sesión.
        Column(modifier = Modifier.padding(24.dp)) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(8.dp))
            Text("Usuario de prueba", style = MaterialTheme.typography.titleMedium)
            Text(
                "usuario@correo.com",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        pantallasMenuLateral.forEach { pantalla ->
            NavigationDrawerItem(
                icon = { Icon(pantalla.icono, contentDescription = null) },
                label = { Text(pantalla.titulo) },
                selected = rutaActual == pantalla.route,
                onClick = { onSeleccionar(pantalla) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            )
        }

        // Empuja "Cerrar sesión" hasta abajo del menú.
        Spacer(Modifier.weight(1f))
        HorizontalDivider()
        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = onCerrarSesion,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
        Spacer(Modifier.height(12.dp))
    }
}
