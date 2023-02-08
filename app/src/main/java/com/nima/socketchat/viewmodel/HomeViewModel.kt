package com.nima.socketchat.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.socketchat.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MessageRepository)
    :ViewModel(){

        fun getSessions() = repository.getSessions().distinctUntilChanged()
}