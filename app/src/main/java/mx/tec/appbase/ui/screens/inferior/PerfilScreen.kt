package mx.tec.appbase.ui.screens.inferior

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.appbase.data.remote.Usuario

// Muestra el usuario que regresó GET /perfil al iniciar sesión.
@Composable
fun PerfilScreen(usuario: Usuario?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(16.dp))

        if (usuario == null) {
            Text("No hay sesión iniciada", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text(usuario.correo, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            AssistChip(
                onClick = {},
                label = { Text("Rol: ${usuario.rol}") },
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Id: ${usuario.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
