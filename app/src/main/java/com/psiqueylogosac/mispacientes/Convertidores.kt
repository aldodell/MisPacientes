package com.psiqueylogosac.mispacientes

import androidx.room.TypeConverter
import java.util.*


class Convertidores {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}