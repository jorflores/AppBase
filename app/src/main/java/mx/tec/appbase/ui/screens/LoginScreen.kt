package mx.tec.appbase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mx.tec.appbase.ui.viewmodel.SesionViewModel

// LOGIN REAL: valida correo y contraseña contra la API (POST /login).
@Composable
fun LoginScreen(
    sesionViewModel: SesionViewModel,
    onLoginExitoso: () -> Unit,
) {
    // Lo que el usuario escribe es estado de la pantalla.
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // La sesión (cargando, error, token) vive en el ViewModel compartido.
    val estado by sesionViewModel.estado.collectAsState()

    // Cuando llega el token, entramos a la app.
    LaunchedEffect(estado.token) {
        if (estado.token != null) onLoginExitoso()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(16.dp))
            Text("Bienvenido", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                singleLine = true,
                enabled = !estado.cargando,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                singleLine = true,
                enabled = !estado.cargando,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )

            // Mensaje de error (401, sin conexión, etc.)
            estado.error?.let { mensaje ->
                Spacer(Modifier.height(12.dp))
                Text(
                    mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { sesionViewModel.iniciarSesion(correo, contrasena) },
                enabled = !estado.cargando,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (estado.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }

            if (estado.cargando) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Si el servidor estaba dormido, la primera vez puede tardar hasta un minuto.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
