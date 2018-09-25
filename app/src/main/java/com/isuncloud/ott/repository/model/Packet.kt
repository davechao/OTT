package com.isuncloud.ott.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packet")
data class Packet(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "data")
        var data: ByteArray,

        @ColumnInfo(name = "appId")
        var appId: String = "",

        @ColumnInfo(name = "appName")
        var appName: String = "",

        @ColumnInfo(name = "deviceId")
        var deviceId: String = "",

        @ColumnInfo(name = "createTime")
        val createTime: Long = 0
)