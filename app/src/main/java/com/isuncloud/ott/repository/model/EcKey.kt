package com.isuncloud.ott.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eckey")
data class EcKey(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "privateKey")
        var privateKey: String = "",

        @ColumnInfo(name = "publicKey")
        var publicKey: String = ""
)