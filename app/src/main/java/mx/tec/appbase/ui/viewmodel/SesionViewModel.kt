package mx.tec.appbase.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.tec.appbase.data.remote.Usuario
import mx.tec.appbase.data.repository.AuthRepository
import mx.tec.appbase.data.repository.PrefsRepository
import retrofit2.HttpException
import java.io.IOException

// Todo lo que la UI necesita saber de la sesión.
data class SesionUiState(
    val revisandoSesion: Boolean = true, // Al abrir la app: ¿hay un token guardado?
    val cargando: Boolean = false,
    val error: String? = null,
    val token: String? = null,           // Se guarda en DataStore: sobrevive al cerrar la app.
    val usuario: Usuario? = null,
)

// Se crea UNA vez en AppNavigation y se comparte con todas las pantallas.
// Es AndroidViewModel porque PrefsRepository necesita el Context (app) para llegar a DataStore.
class SesionViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AuthRepository()
    private val prefs = PrefsRepository(app)

    private val _estado = MutableStateFlow(SesionUiState())
    val estado: StateFlow<SesionUiState> = _estado.asStateFlow()

    init {
        restaurarSesion()
    }

    // Al abrir la app: si hay un token guardado, entramos directo sin pasar por el login.
    private fun restaurarSesion() {
        viewModelScope.launch {
            val token = prefs.tokenGuardado() // Leer DataStore es rápido (archivo local)
            _estado.update { it.copy(revisandoSesion = false, token = token) }
            // El perfil se pide en segundo plano: la API en Render puede tardar en despertar.
            if (token != null) cargarPerfil(token)
        }
    }

    private suspend fun cargarPerfil(token: String) {
        try {
            val usuario = repository.obtenerPerfil(token)
            _estado.update { it.copy(usuario = usuario) }
        } catch (e: HttpException) {
            // 401: el token expiró o ya no es válido -> hay que iniciar sesión otra vez.
            if (e.code() == 401) cerrarSesion()
        } catch (e: IOException) {
            // Sin conexión: conservamos la sesión; el perfil se cargará la próxima vez.
        } catch (e: Exception) {
            // Cualquier otro error: tampoco cerramos la sesión, solo no hay perfil.
        }
    }

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
                // 3. Guardamos el token en DataStore para la próxima vez que se abra la app
                prefs.guardarToken(token)
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
        // Primero en memoria (la UI reacciona de inmediato) y luego en DataStore.
        _estado.value = SesionUiState(revisandoSesion = false)
        viewModelScope.launch { prefs.borrarToken() }
    }
}
