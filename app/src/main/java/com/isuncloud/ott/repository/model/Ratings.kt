package com.isuncloud.ott.repository.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ratings(

        @SerializedName("appSTime")
        var appSTime: Long = 0,

        @SerializedName("appETime")
        var appETime: Long = 0,

        @SerializedName("appRunduration")
        var appRunduration: Long = 0

): Parcelable