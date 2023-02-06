package com.nima.socketchat

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeDataStore (private val context: Context) {
    companion object{

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("theme")

        val themeKey = intPreferencesKey("appTheme")
    }

    val getTheme: Flow<Int?> = context.dataStore.data
        .map {
            it[themeKey] ?: 0
        }

    suspend fun saveTheme(theme: Int){
        context.dataStore.edit {
            it[themeKey] = theme
        }
    }
}