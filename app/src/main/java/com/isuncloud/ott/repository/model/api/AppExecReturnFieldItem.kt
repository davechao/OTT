package com.isuncloud.ott.repository.model.api

import com.google.gson.annotations.SerializedName

data class AppExecReturnFieldItem(

        @SerializedName("ok")
        val ok: Boolean = false,

        @SerializedName("uuid")
        val uuid: String = ""

)