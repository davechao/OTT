package com.isuncloud.ott.repository.model.rn

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductModel(

        @SerializedName("storeName")
        var storeName: String = "",

        @SerializedName("itemName")
        var itemName: String = "",

        @SerializedName("price")
        var price: Double = 0.toDouble()

): Parcelable