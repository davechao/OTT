package com.isuncloud.ott.repository.model.rn

import com.google.gson.annotations.SerializedName

data class EnvRequest(

        @SerializedName("serverUrl")
        val serverUrl: String = "",

        @SerializedName("nodeUrl")
        val nodeUrl: String = "",

        @SerializedName("web3Url")
        val web3Url: String = "",

        @SerializedName("privateKey")
        val privateKey: String? = "",

        @SerializedName("storage")
        val storage: String = ""

): BaseModel()