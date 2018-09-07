package com.isuncloud.ott.repository.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "wallet")
data class Wallet(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "address")
        var address: String = "",

        @ColumnInfo(name = "currency")
        var currency: Double = 0.toDouble()
)