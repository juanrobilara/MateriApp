package com.jurobil.materiapp.ui.core

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemePreferences {
    private const val PREF_NAME = "theme_prefs"
    private const val KEY_DARK_MODE = "dark_mode"

    private val Context.dataStore by preferencesDataStore(name = PREF_NAME)

    suspend fun setDarkMode(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(KEY_DARK_MODE)] = enabled
        }
    }

    fun isDarkModeFlow(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { prefs -> prefs[booleanPreferencesKey(KEY_DARK_MODE)] ?: false }
    }
}
