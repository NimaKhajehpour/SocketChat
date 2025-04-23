package com.nima.socketchat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.socketchat.model.message.ChatMessage
import com.nima.socketchat.model.user.LocalUser
import com.nima.socketchat.repository.MessageRepository
import com.nima.socketchat.utils.ClientSocketManager
import com.nima.socketchat.utils.UserIdentityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ClientUserChatViewModel (private val socketManager: ClientSocketManager, private val repository: MessageRepository,
    private val userManager: UserIdentityManager): ViewModel() {

    var user by mutableStateOf<LocalUser?>(null)
        private set
    private val _connectedUsers = MutableStateFlow<List<LocalUser>>(emptyList())
    val connectedUsers: StateFlow<List<LocalUser>> = _connectedUsers

    init {
        viewModelScope.launch {
            user = userManager.getUser()
        }
        observeConnections()
    }

    private fun observeConnections() {
        viewModelScope.launch {
            socketManager.userListFlow.collect { userList ->
                _connectedUsers.value = userList
            }
        }
    }

    suspend fun sendMessage(message: ChatMessage) = socketManager.sendMessage(message)
    fun getUserMessages(id: String) = repository.getMessages(id).distinctUntilChanged()
}