package com.nima.socketchat.database

import androidx.room.TypeConverter
import java.util.*

class TypeConverter {

    @TypeConverter
    fun fromUUID(uuid: UUID) = uuid.toString()

    @TypeConverter
    fun toUUID(id: String) = UUID.fromString(id)

    @TypeConverter
    fun fromBoolean(bool: Boolean) = if (bool) 1 else 0

    @TypeConverter
    fun toBoolean(bool: Int) = bool == 1
}