package id.ac.istts.ecotong.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager(context: Context) {

    private val dataStore = context.dataStore

    suspend fun setApiToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ECOTONG_JWT_TOKEN] = token
        }
    }

    suspend fun removeToken() {
        dataStore.edit { prefs ->
            prefs.remove(ECOTONG_JWT_TOKEN)
        }
    }


    val ecotongJwtToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ECOTONG_JWT_TOKEN]
    }

    companion object {
        private val Context.dataStore by preferencesDataStore("settings")
        val ECOTONG_JWT_TOKEN = stringPreferencesKey("ecotong_jwt_token")
    }

}