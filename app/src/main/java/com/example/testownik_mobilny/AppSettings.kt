package com.example.testownik_mobilny

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore by preferencesDataStore("AppSettings")

class AppSettings(private val context: Context) {

    // Common error handling for DataStore flows
    private fun <T> Flow<T>.handleErrors(): Flow<T> = this.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences() as T)
        } else {
            throw exception
        }
    }

    val fontSize: Flow<FontSize> = context.dataStore.data
        .handleErrors()
        .map { preferences ->
            val fontSizeName = preferences[fontSizeKey] ?: FontSize.MEDIUM.name
            try {
                FontSize.valueOf(fontSizeName)
            } catch (e: IllegalArgumentException) {
                FontSize.MEDIUM
            }
        }

    val debugEnable: Flow<Boolean> = context.dataStore.data
        .handleErrors()
        .map { preferences ->
            preferences[debugEnableKey] ?: false
        }

    suspend fun setFontSize(size: FontSize) {
        context.dataStore.edit { settings ->
            settings[fontSizeKey] = size.name
        }
    }

    suspend fun setDebugEnable(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[debugEnableKey] = value
        }
    }

    companion object {
        val fontSizeKey = stringPreferencesKey("font_size_key")
        val debugEnableKey = booleanPreferencesKey("debug_enable_key")
    }
}

enum class FontSize {
    SMALL, MEDIUM, LARGE
}