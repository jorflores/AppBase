package mx.tec.appbase.data.local

import androidx.datastore.preferences.core.stringPreferencesKey

// Claves de lo que guardamos en DataStore.
// El texto ("token") es el nombre dentro del archivo: si lo cambias,
// la app ya no encuentra lo que se guardó antes.
object PrefKeys {
    val TOKEN = stringPreferencesKey("token")
}
