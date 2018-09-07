package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class UpdateAppExecRecordItem(

        @SerializedName("uuid")
        val uuid: String = "",

        @SerializedName("deviceId")
        val deviceId: String = "",

        @SerializedName("ethAddress")
        val ethAddress: String = "",

        @SerializedName("endTime")
        val endTime: Long = 0,

        @SerializedName("lightTxJSON")
        val lightTxJson: String = ""

)