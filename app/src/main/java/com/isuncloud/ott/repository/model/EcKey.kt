package com.isuncloud.ott.repository.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "eckey")
data class EcKey(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "privateKey")
        var privateKey: String = "",

        @ColumnInfo(name = "publicKey")
        var publicKey: String = ""
)