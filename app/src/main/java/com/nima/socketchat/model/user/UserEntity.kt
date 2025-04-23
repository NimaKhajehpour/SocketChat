package com.nima.socketchat.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uuid: UUID,
    val username: String
)
