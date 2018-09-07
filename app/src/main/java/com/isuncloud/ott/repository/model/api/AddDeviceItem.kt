package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class AddDeviceItem(

        @SerializedName("deviceId")
        val deviceId: String = "",

        @SerializedName("ethAddress")
        val ethAddress: String = "",

        @SerializedName("status")
        val status: String = ""

)