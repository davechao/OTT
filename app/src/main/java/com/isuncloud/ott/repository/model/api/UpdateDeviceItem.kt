package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class UpdateDeviceItem(

        @SerializedName("deviceId")
        val deviceId: String = "",

        @SerializedName("ethAddress")
        val ethAddress: String = "",

        @SerializedName("status")
        val status: String = "",

        @SerializedName("lastAccessTime")
        val lastAccessTime: Long = 0

)