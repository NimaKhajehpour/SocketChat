package com.nima.socketchat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.socketchat.model.user.LocalUser
import com.nima.socketchat.repository.MessageRepository
import com.nima.socketchat.utils.UserIdentityManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class LocalUserChatViewModel (private val identityManager: UserIdentityManager, private val repository: MessageRepository): ViewModel() {

    var user by mutableStateOf<LocalUser?>(null)
        private set

    init {
        viewModelScope.launch {
            user = identityManager.getUser()
        }
    }

    fun getUserMessages(id: String) = repository.getMessages(id).distinctUntilChanged()
}