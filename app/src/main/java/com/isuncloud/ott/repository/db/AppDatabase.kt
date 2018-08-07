package com.isuncloud.ott.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.RoomDatabase
import com.isuncloud.ott.database.typeconverter.LocalDateTimeConverter
import com.isuncloud.ott.repository.db.dao.EcKeyDao
import com.isuncloud.ott.repository.model.EcKey

@Database(
        entities = [
            EcKey::class
        ],
        version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ecKeyDao(): EcKeyDao
}