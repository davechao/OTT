package com.isuncloud.isuntvmall.api.model

import com.google.gson.annotations.SerializedName

open class ApiBaseItem(

        @SerializedName("status")
        val status: Boolean = false,

        @SerializedName("msg")
        val msg: String = "",

        @SerializedName("errcode")
        val errorCode: String = "") {
}