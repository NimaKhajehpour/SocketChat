package com.nima.socketchat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nima.socketchat.model.chat.ChatSessionEntity
import com.nima.socketchat.model.message.MessageEntity
import com.nima.socketchat.model.user.UserEntity

@Database(
    entities = [UserEntity::class, ChatSessionEntity::class, MessageEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatSessionDao
    abstract fun messageDao(): MessageDao
}