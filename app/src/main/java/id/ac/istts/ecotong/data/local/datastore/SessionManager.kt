package id.ac.istts.ecotong.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import id.ac.istts.ecotong.data.remote.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class SessionManager(context: Context) {

    private val dataStore = context.dataStore

    suspend fun setApiToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ECOTONG_JWT_TOKEN] = token
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[PROFILE_NAME] = user.name
            preferences[PROFILE_EMAIL] = user.email
            preferences[PROFILE_USERNAME] = user.username
            preferences[PROFILE_IMAGE] = user.profilePicture

        }
    }

    suspend fun getProfile(): User? {
        val name = dataStore.data.map { preferences ->
            preferences[PROFILE_NAME]
        }.firstOrNull()
        val email = dataStore.data.map { preferences ->
            preferences[PROFILE_EMAIL]
        }.firstOrNull()
        val username = dataStore.data.map { preferences ->
            preferences[PROFILE_USERNAME]
        }.firstOrNull()
        val image = dataStore.data.map { preferences ->
            preferences[PROFILE_IMAGE]
        }.firstOrNull()

        if (name == null || email == null || username == null || image == null) {
            return null
        }
        return User(
            name = name, email = email, username = username, profilePicture = image
        )
    }

    suspend fun logout() {
        dataStore.edit { prefs ->
            prefs.remove(ECOTONG_JWT_TOKEN)
            prefs.remove(PROFILE_NAME)
            prefs.remove(PROFILE_EMAIL)
            prefs.remove(PROFILE_IMAGE)
        }
    }


    val ecotongJwtToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ECOTONG_JWT_TOKEN]
    }

    companion object {
        private val Context.dataStore by preferencesDataStore("settings")
        val ECOTONG_JWT_TOKEN = stringPreferencesKey("ecotong_jwt_token")
        val PROFILE_NAME = stringPreferencesKey("profile_name")
        val PROFILE_USERNAME = stringPreferencesKey("profile_username")
        val PROFILE_EMAIL = stringPreferencesKey("profile_email")
        val PROFILE_IMAGE = stringPreferencesKey("profile_image")
    }

}