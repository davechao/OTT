package com.isuncloud.isuntvmall.api.model

import com.google.gson.annotations.SerializedName

class ApiBaseListItem<T> : ApiBaseItem() {

    @SerializedName("data")
    val itemList: ArrayList<T>? = arrayListOf()
        get() {
            return field ?: arrayListOf()
        }
}