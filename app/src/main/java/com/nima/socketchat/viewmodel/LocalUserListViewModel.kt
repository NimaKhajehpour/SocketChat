package com.nima.socketchat.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.socketchat.repository.MessageRepository
import kotlinx.coroutines.flow.distinctUntilChanged

class LocalUserListViewModel (private val repository: MessageRepository): ViewModel() {

    fun getAllUsers() = repository.getAllUsers().distinctUntilChanged()
}