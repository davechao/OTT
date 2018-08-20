package com.isuncloud.ott.repository.model.rn

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

@Parcelize
data class MetaDataModel(

        @SerializedName("time")
        var time: LocalDateTime = LocalDateTime.now(),

        @SerializedName("memo")
        var memo: String = "",

        @SerializedName("product_info")
        var productInfo: ProductModel = ProductModel()

): Parcelable