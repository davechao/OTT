package com.isuncloud.isuntvmall.api.model

import com.google.gson.annotations.SerializedName

open class ApiBaseItem(

        @SerializedName("result")
        val result: Boolean = false,

        @SerializedName("message")
        val msg: String = "") {

}