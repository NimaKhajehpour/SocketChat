package com.nima.socketchat

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoundedCornerDataStore (private val context: Context) {
    companion object{

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dp")

        val dpKey = floatPreferencesKey("dpKey")
    }

    val getDp: Flow<Float?> = context.dataStore.data
        .map {
            it[dpKey] ?: 0f
        }

    suspend fun saveDp(dp: Float){
        context.dataStore.edit {
            it[dpKey] = dp
        }
    }
}