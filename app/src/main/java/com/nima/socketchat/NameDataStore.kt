package com.nima.socketchat

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NameDataStore (private val context: Context){
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("name")

        val nameKey = stringPreferencesKey("username")
    }

    val getUsername: Flow<String?> = context.dataStore.data
        .map { pref ->
            pref[nameKey] ?: ""
        }

    suspend fun saveUsername (username: String){
        context.dataStore.edit{
            it[nameKey] = username
        }
    }
}