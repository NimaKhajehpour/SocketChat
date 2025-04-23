package com.nima.socketchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.socketchat.model.user.LocalUser
import com.nima.socketchat.utils.UserIdentityManager
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import java.util.UUID

class UserIdentityViewModel (private val userIdentityManager: UserIdentityManager): ViewModel() {

    fun saveUsername(username: String, onComplete: () -> Unit) = viewModelScope.launch {
        val uuid = UUID.randomUUID()
        userIdentityManager.setUsername(username)
        userIdentityManager.setUserId(uuid)
        onComplete()
    }

    fun getUser() = viewModelScope.produce<LocalUser> {
        userIdentityManager.getUser()
    }
}