package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.isuncloud.ott.utils.RawJsonGsonAdapter

data class UpdateAppExecRecordItem(

        @SerializedName("uuid")
        val uuid: String = "",

        @SerializedName("deviceId")
        val deviceId: String = "",

        @SerializedName("ethAddress")
        val ethAddress: String = "",

        @SerializedName("endTime")
        val endTime: Long = 0,

        @JsonAdapter(RawJsonGsonAdapter::class)
        @SerializedName("lightTxJSON")
        val lightTxJson: String = ""

)