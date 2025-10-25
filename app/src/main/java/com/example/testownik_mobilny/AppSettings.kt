package com.example.testownik_mobilny

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException


val Context.dataStore by preferencesDataStore("AppSettings")

class AppSettings(
    private val context : Context
) {
    val fontSize = context.dataStore.data.catch { exeption ->
        if (exeption is IOException){
            emit(emptyPreferences())
        }else{
            throw exeption
        }
    }.map { preferences ->
        val fontSize = preferences[fontSizeKey] ?: FontSize.MEDIUM.name

        try{
            FontSize.valueOf(fontSize)
        }catch (e: IllegalArgumentException){
            FontSize.MEDIUM
        }
    }

    suspend fun setFontSize(size: FontSize){
        context.dataStore.edit { settings ->
            settings[fontSizeKey] = size.toString()
        }
    }

    companion object{
        val fontSizeKey = stringPreferencesKey("font_size_key")
    }
}

enum class FontSize{
    SMALL,
    MEDIUM,
    LARGE,
}

//fun FontSize.toTextUnit() : TextUnit{
//    return when(this){
//        FontSize.SMALL -> 16.sp
//        FontSize.MEDIUM -> 24.sp
//        FontSize.LARGE -> 32.sp
//    }
//}
