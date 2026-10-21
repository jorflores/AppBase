package mx.tec.appbase.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

// TopAppBar todavía está marcada como experimental en Material 3,
// por eso se necesita @OptIn.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(titulo: String, onAbrirMenu: () -> Unit) {
    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            IconButton(onClick = onAbrirMenu) {
                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    )
}
