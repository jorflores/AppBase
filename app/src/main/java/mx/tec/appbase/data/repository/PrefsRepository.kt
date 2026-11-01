package mx.tec.appbase.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import mx.tec.appbase.data.local.PrefKeys
import mx.tec.appbase.data.local.dataStore

// Lee y escribe en DataStore. El ViewModel no sabe que existe un archivo.
class PrefsRepository(private val context: Context) {

    // Lectura reactiva: emite un valor nuevo cada vez que el token cambia.
    // null = nunca se guardó un token (o se borró al cerrar sesión).
    val tokenFlow: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[PrefKeys.TOKEN] }

    // Lectura de una sola vez: first() toma el valor actual y termina.
    suspend fun tokenGuardado(): String? = tokenFlow.first()

    suspend fun guardarToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.TOKEN] = token
        }
    }

    suspend fun borrarToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(PrefKeys.TOKEN)
        }
    }
}
