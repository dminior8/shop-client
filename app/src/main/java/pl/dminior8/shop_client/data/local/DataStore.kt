package pl.dminior8.shop_client.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.UUID
import java.util.UUID.*

// konifugiracja instancji DataStore o nazwie "user_prefs", do której dostęp przez dowolny obiekt Context
// (rozszerzony o właściwość userIdDataStore); preferencesDataStore - do tego delegowanie zarządzania właścowością (gettery, settery)
val Context.userIdDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_prefs"
)

// sprawdza czy userId w DataStore, jeśli nie to generuje
suspend fun getOrCreateUserId(context: Context): UUID {
    val key = stringPreferencesKey("user_id")
    val prefs = context.userIdDataStore.data.first()
    val currentId = prefs[key]
    return if (currentId != null) {
        fromString(currentId)
    } else {
        val newId = randomUUID()
        context.userIdDataStore.edit { it[key] = newId.toString() }
        newId
    }
}
