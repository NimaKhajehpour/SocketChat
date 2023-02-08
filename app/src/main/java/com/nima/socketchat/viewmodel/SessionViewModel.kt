package com.nima.socketchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.socketchat.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(private val repository: MessageRepository)
    :ViewModel(){

    fun getSessionMessage(fk: UUID) = repository.getSessionMessages(fk).distinctUntilChanged()

    fun deleteSession(id: UUID) = viewModelScope.launch{ repository.deleteSession(id) }

    fun deleteSessionMessages(fk: UUID) = viewModelScope.launch { repository.deleteSessionMessages(fk) }
}