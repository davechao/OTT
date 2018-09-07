package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class InsertAppExecRecordItem(

        @SerializedName("deviceId")
        val deviceId: String = "",

        @SerializedName("ethAddress")
        val ethAddress: String = "",

        @SerializedName("appid")
        val appid: String = "",

        @SerializedName("appName")
        val appName: String = "",

        @SerializedName("startTime")
        val startTime: Long = 0

)