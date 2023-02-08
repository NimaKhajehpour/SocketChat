package com.nima.socketchat.repository

import com.nima.socketchat.database.MessageDao
import com.nima.socketchat.model.Message
import com.nima.socketchat.model.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class MessageRepository @Inject constructor(private val dao: MessageDao) {

    fun getCurrentSession(): Flow<Session> =
        dao.getCurrentSession().flowOn(Dispatchers.IO).conflate()

    fun getSessionMessages(fk: UUID): Flow<List<Message>> =
        dao.getSessionMessages(fk).flowOn(Dispatchers.IO).conflate()

    suspend fun addMessage(message: Message) =
        dao.addMessage(message)

    suspend fun addSession(session: Session) =
        dao.addSession(session)

    suspend fun updateSession(session: Session) =
        dao.updateSession(session)

    fun getSessions(): Flow<List<Session>> =
        dao.getSessions().flowOn(Dispatchers.IO).conflate()

    suspend fun deleteSession(id: UUID) =
        dao.deleteSession(id)

    suspend fun deleteSessionMessages(fk: UUID) =
        dao.deleteSessionMessages(fk)
}