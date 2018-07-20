package com.isuncloud.isuntvmall.database.typeconverter

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class LocalDateTimeConverter {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @TypeConverter
    fun dateToTimestamp(localDateTime: LocalDateTime): Long {
        return if(localDateTime != null) {
            localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
        } else {
            LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
        }
    }

    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        return if (value != null) {
            Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime()
        } else {
            LocalDateTime.now()
        }
    }

}
