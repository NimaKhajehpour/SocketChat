package com.nima.socketchat.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val USERNAME = stringPreferencesKey("username")
    val USER_ID = stringPreferencesKey("user_id")
}