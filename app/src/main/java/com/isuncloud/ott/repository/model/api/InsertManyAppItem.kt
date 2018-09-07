package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class InsertManyAppItem(

        @SerializedName("deviceId")
        val deviceId: String = "",

        @SerializedName("ethAddress")
        val ethAddress: String = "",

        @SerializedName("apps")
        val apps: ArrayList<AppItem> = arrayListOf()

)