package com.nima.socketchat.model.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chat_sessions")
data class ChatSessionEntity(
    @PrimaryKey val chatId: UUID,
    val participantIds: List<UUID>
)
