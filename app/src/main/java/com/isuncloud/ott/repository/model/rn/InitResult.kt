package com.isuncloud.ott.repository.model.rn

import com.google.gson.annotations.SerializedName

data class InitResult(

        @SerializedName("privateKey")
        val privateKey: String = "",

        @SerializedName("address")
        val address: String = ""

): BaseModel()