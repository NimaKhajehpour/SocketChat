package com.nima.socketchat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Session(
    @PrimaryKey
    val sessionId: UUID = UUID.randomUUID(),
    @ColumnInfo
    var currentSession: Boolean
)
