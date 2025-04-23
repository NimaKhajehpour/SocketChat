package com.nima.socketchat.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.UUID

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toUUID(uuid: String): UUID = UUID.fromString(uuid)

    @TypeConverter
    fun fromUUIDList(list: List<UUID>): String = list.joinToString(",")

    @TypeConverter
    fun toUUIDList(data: String): List<UUID> =
        if (data.isEmpty()) emptyList()
        else data.split(",").map { UUID.fromString(it) }
}