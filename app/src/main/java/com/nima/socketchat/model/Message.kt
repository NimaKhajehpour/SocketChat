package com.nima.socketchat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Message(
    @PrimaryKey
    val messageId: UUID = UUID.randomUUID(),
    @ColumnInfo
    val senderName: String,
    @ColumnInfo
    val messageBody: String,
    @ColumnInfo
    val received: Boolean,
    @ColumnInfo
    val foreignKey: UUID
)
