package com.nima.socketchat.model.message

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val type: String = "MESSAGE", // MESSAGE, ACK, READ
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val timestamp: Long,
    val content: String? = null, // Only for MESSAGE
    val messageId: String,
    val contactName: String,
    val username: String
)

