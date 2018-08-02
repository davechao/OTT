package com.isuncloud.isuntvmall.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.RoomDatabase
import com.isuncloud.isuntvmall.database.typeconverter.LocalDateTimeConverter

//@Database(
//        entities = [],
//        version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

}