package com.isuncloud.ott.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.RoomDatabase
import com.isuncloud.ott.repository.db.dao.EcKeyDao
import com.isuncloud.ott.repository.db.dao.WalletDao
import com.isuncloud.ott.repository.db.typeconverter.LocalDateTimeConverter
import com.isuncloud.ott.repository.model.EcKey
import com.isuncloud.ott.repository.model.Wallet

@Database(
        entities = [
            EcKey::class,
            Wallet::class
        ],
        version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ecKeyDao(): EcKeyDao
    abstract fun walletDao(): WalletDao
}