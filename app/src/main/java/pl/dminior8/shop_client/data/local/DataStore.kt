package pl.dminior8.shop_client.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.UUID

val Context.userIdDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "user_prefs"
)

suspend fun getOrCreateUserId(context: Context): UUID {
    val key = stringPreferencesKey("user_id")
    val prefs = context.userIdDataStore.data.first()
    val currentId = prefs[key]
    return if (currentId != null) {
        UUID.fromString(currentId)
    } else {
        val newId = UUID.randomUUID()
        context.userIdDataStore.edit { it[key] = newId.toString() }
        newId
    }
}
