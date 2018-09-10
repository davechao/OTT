package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class AppItem(

        @SerializedName("appId")
        val appId: String = "",

        @SerializedName("appName")
        val appName: String = ""

)