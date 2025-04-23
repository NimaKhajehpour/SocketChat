package com.nima.socketchat.repository

import com.nima.socketchat.database.MessageDao
import com.nima.socketchat.model.message.ChatMessage
import com.nima.socketchat.utils.Utilities.toEntity
import com.nima.socketchat.utils.Utilities.toChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.UUID

class MessageRepository(
    private val messageDao: MessageDao
) {
    suspend fun saveMessage(message: ChatMessage) {
         messageDao.insertMessage(message.toEntity())
    }

    fun getMessages(chatId: String): Flow<List<ChatMessage>> {
        return messageDao.getMessagesForChat(UUID.fromString(chatId))
            .map { it -> it.map { it.toChatMessage() } }.distinctUntilChanged()
    }

    fun getAllUsers() = messageDao.getAllUsers().distinctUntilChanged()
}
