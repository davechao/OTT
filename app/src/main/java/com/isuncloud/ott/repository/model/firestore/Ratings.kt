package com.isuncloud.ott.repository.model.firestore

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ratings(

        @SerializedName("appSTime")
        var appSTime: String = "",

        @SerializedName("appETime")
        var appETime: String = "",

        @SerializedName("appRunduration")
        var appRunduration: Long = 0

): Parcelable