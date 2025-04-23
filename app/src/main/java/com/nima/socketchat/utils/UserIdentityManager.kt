package com.nima.socketchat.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nima.socketchat.model.user.LocalUser
import kotlinx.coroutines.flow.first
import java.util.UUID

class UserIdentityManager (private val datastore: DataStore<Preferences>) {

    suspend fun getUser(): LocalUser {
        val prefs = datastore.data.first()
        val uuid = prefs[DataStoreKeys.USER_ID]?.let { UUID.fromString(it) }
        val username = prefs[DataStoreKeys.USERNAME]
        return LocalUser(uuid!!, username!!)
    }

    suspend fun isUserInitialized(): Boolean {
        val prefs = datastore.data.first()
        return prefs[DataStoreKeys.USERNAME] != null &&
                prefs[DataStoreKeys.USER_ID] != null
    }


    suspend fun setUsername(username: String) {
        datastore.edit {
            it[DataStoreKeys.USERNAME] = username
        }
    }

    suspend fun setUserId(userId: UUID) {
        datastore.edit {
            it[DataStoreKeys.USER_ID] = userId.toString()
        }
    }

    suspend fun getUsername(): UUID{
        val prefs = datastore.data.first()
        val username = prefs[DataStoreKeys.USERNAME]
        return UUID.fromString(username!!)
    }
}