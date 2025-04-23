package com.nima.socketchat.repository

import com.nima.socketchat.database.ChatSessionDao
import com.nima.socketchat.model.chat.ChatSessionEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ChatRepository(
    private val chatDao: ChatSessionDao
) {
    suspend fun saveChatSession(session: ChatSessionEntity) = chatDao.insertSession(session)

    suspend fun getChatById(id: UUID): ChatSessionEntity? = chatDao.getChatById(id)

    fun getAllChatSessions(): Flow<List<ChatSessionEntity>> = chatDao.getAllSessions()
}
