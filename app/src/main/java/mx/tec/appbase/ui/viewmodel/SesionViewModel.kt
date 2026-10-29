package mx.tec.appbase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.tec.appbase.data.remote.Usuario
import mx.tec.appbase.data.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException

// Todo lo que la UI necesita saber de la sesión.
data class SesionUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val token: String? = null,     // Por ahora vive en memoria; más adelante, en DataStore.
    val usuario: Usuario? = null,
)

// Se crea UNA vez en AppNavigation y se comparte con todas las pantallas.
class SesionViewModel(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _estado = MutableStateFlow(SesionUiState())
    val estado: StateFlow<SesionUiState> = _estado.asStateFlow()

    fun iniciarSesion(correo: String, password: String) {
        if (correo.isBlank() || password.isBlank()) {
            _estado.update { it.copy(error = "Escribe tu correo y tu contraseña") }
            return
        }
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            try {
                // 1. Login: correo + password -> token
                val token = repository.iniciarSesion(correo.trim(), password)
                // 2. Con el token pedimos el perfil (prueba de que el token sirve)
                val usuario = repository.obtenerPerfil(token)
                _estado.update { it.copy(cargando = false, token = token, usuario = usuario) }
            } catch (e: HttpException) {
                val mensaje = when (e.code()) {
                    400 -> "Escribe tu correo y tu contraseña"
                    401 -> "Correo o contraseña incorrectos"
                    else -> "Error del servidor (${e.code()})"
                }
                _estado.update { it.copy(cargando = false, error = mensaje) }
            } catch (e: IOException) {
                _estado.update {
                    it.copy(cargando = false, error = "No se pudo conectar con el servidor")
                }
            } catch (e: Exception) {
                _estado.update { it.copy(cargando = false, error = "Error inesperado: ${e.message}") }
            }
        }
    }

    fun cerrarSesion() {
        _estado.value = SesionUiState()
    }
}
