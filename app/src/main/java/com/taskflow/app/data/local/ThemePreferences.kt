package com.taskflow.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

/**
 * Menyimpan preferensi tema user (dark mode on/off).
 * null = ikut sistem (default).
 */
class ThemePreferences(private val context: Context) {

    private val darkModeKey = booleanPreferencesKey("dark_mode")

    val isDarkMode: Flow<Boolean?> = context.dataStore.data.map { prefs ->
        prefs[darkModeKey]
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[darkModeKey] = enabled
        }
    }

    suspend fun clearDarkMode() {
        context.dataStore.edit { prefs ->
            prefs.remove(darkModeKey)
        }
    }
}
