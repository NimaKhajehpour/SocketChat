package com.nima.socketchat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nima.socketchat.model.Message
import com.nima.socketchat.model.Session
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface MessageDao {

    @Query("select * from session where currentSession=1")
    fun getCurrentSession(): Flow<Session>

    @Query("select * from message where foreignKey=:fk")
    fun getSessionMessages(fk: UUID): Flow<List<Message>>

    @Query("select * from session")
    fun getSessions(): Flow<List<Session>>

    @Query("delete from session where sessionId = :id")
    suspend fun deleteSession(id: UUID)

    @Query("delete from message where foreignKey = :fk")
    suspend fun deleteSessionMessages(fk: UUID)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSession(session: Session)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSession(session: Session)
}