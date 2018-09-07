package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class AppItem(

        @SerializedName("appid")
        val appid: String = "",

        @SerializedName("appName")
        val appName: String = ""

)