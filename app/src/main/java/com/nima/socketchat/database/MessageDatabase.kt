package com.nima.socketchat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nima.socketchat.model.Message
import com.nima.socketchat.model.Session

@TypeConverters(TypeConverter::class)
@Database(version = 1, entities = [Message::class, Session::class])
abstract class MessageDatabase: RoomDatabase(){

    abstract fun dao(): MessageDao
}