package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class ApiItem<T>(

        @SerializedName("result")
        val result: Int = 0,

        @SerializedName("message")
        val message: String = "",

        @SerializedName("data")
        val data: T
)