package com.example.gearhubmobile.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager(private val context: Context) {

    companion object {
        private val JWT_TOKEN = stringPreferencesKey("jwt_token")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[JWT_TOKEN] = token
        }
    }

    val token: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[JWT_TOKEN] }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(JWT_TOKEN)
        }
    }
}