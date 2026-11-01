package mx.tec.appbase.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Nombre del archivo donde DataStore guarda los datos dentro del teléfono:
// /data/data/mx.tec.appbase/files/datastore/sesion_prefs.preferences_pb
private const val NOMBRE_ARCHIVO = "sesion_prefs"

// La "puerta" al archivo. Se declara UNA sola vez, fuera de cualquier clase.
// Si se crean dos instancias del mismo archivo, la app truena.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NOMBRE_ARCHIVO)
