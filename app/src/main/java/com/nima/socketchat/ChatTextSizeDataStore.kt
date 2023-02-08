package com.nima.socketchat

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatTextSizeDataStore(private val context: Context) {
    companion object{

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("textSize")

        val spKey = floatPreferencesKey("spKey")
    }

    val getTextSize: Flow<Float?> = context.dataStore.data
        .map {
            it[spKey] ?: 9f
        }

    suspend fun saveTextSize(size: Float){
        context.dataStore.edit {
            it[spKey] = size
        }
    }
}