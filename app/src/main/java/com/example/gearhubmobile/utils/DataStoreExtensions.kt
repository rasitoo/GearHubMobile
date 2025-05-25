package com.example.gearhubmobile.utils

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")
