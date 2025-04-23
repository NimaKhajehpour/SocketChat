package com.nima.socketchat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.socketchat.utils.ClientSocketManager
import com.nima.socketchat.utils.UserIdentityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JoinServerViewModel (
    private val clientSocketManager: ClientSocketManager,
    private val userIdentityManager: UserIdentityManager
): ViewModel() {

    private val _uiState = MutableStateFlow<JoinServerUiState>(JoinServerUiState.Idle)
    val uiState: StateFlow<JoinServerUiState> = _uiState

    suspend fun connectToServer(ip: String, password: String) {
        Log.d("JoinServerViewModel", "Result from connect: got called")
        viewModelScope.launch {
            Log.d("JoinServerViewModel", "Result from connect: got called again")
            _uiState.value = JoinServerUiState.Loading
            Log.d("JoinServerViewModel", "Result from connect: got loading")
            val user = userIdentityManager.getUser()

            Log.d("JoinServerViewModel", "Result from connect: got user")

            val connected = clientSocketManager.connectToServer(ip, password, user)
            Log.d("JoinServerViewModel", "Result from connect: $connected")
            _uiState.value = if (connected) {
                JoinServerUiState.Success
            } else {
                JoinServerUiState.Error("Connection failed. Check IP or password.")
            }
        }
    }
}

sealed class JoinServerUiState {
    object Idle : JoinServerUiState()
    object Loading : JoinServerUiState()
    object Success : JoinServerUiState()
    data class Error(val message: String) : JoinServerUiState()
}
