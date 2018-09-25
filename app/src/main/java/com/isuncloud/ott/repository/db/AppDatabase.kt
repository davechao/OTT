package com.isuncloud.ott.repository.db

import androidx.room.Database
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import com.isuncloud.ott.repository.db.dao.EcKeyDao
import com.isuncloud.ott.repository.db.dao.PacketDao
import com.isuncloud.ott.repository.db.dao.WalletDao
import com.isuncloud.ott.repository.db.typeconverter.LocalDateTimeConverter
import com.isuncloud.ott.repository.model.EcKey
import com.isuncloud.ott.repository.model.Packet
import com.isuncloud.ott.repository.model.Wallet

@Database(
        entities = [
            EcKey::class,
            Wallet::class,
            Packet::class
        ],
        version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ecKeyDao(): EcKeyDao
    abstract fun walletDao(): WalletDao
    abstract fun packetDao(): PacketDao
}