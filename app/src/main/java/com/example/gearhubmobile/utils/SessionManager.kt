package com.example.gearhubmobile.utils

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val JWT_TOKEN = stringPreferencesKey("jwt_token")
    }

    val token: Flow<String?> = context.dataStore.data.map { prefs -> prefs[JWT_TOKEN] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs -> prefs[JWT_TOKEN] = token }
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs -> prefs.remove(JWT_TOKEN) }
    }

    private fun decodeJwt(token: String): Triple<String, String, Int>? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null
            val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING))
            val payload = JSONObject(payloadJson)

            val userId = payload.optString("nameid") ?: return null
            val email = payload.optString("email") ?: return null
            val userType = payload.optInt("UserType", -1).takeIf { it != -1 } ?: return null

            Triple(userId, email, userType)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCurrentToken(): String? {
        return context.dataStore.data.firstOrNull()?.get(JWT_TOKEN)
    }

    suspend fun getUserId(): String? {
        return getCurrentToken()?.let { decodeJwt(it)?.first }
    }

    suspend fun getUserEmail(): String? {
        return getCurrentToken()?.let { decodeJwt(it)?.second }
    }

    suspend fun getUserType(): Int? {
        return getCurrentToken()?.let { decodeJwt(it)?.third }
    }
}


@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
}