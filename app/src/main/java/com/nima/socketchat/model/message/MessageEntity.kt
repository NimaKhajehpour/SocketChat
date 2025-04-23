package com.nima.socketchat.model.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val timestamp: Long,
    val content: String,
    val contactName: String,
    val username: String
)

