package com.isuncloud.ott.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet")
data class Wallet(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "address")
        var address: String = "",

        @ColumnInfo(name = "currency")
        var currency: Double = 0.toDouble()
)