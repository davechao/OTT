package com.isuncloud.ott.repository.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppData(

        @SerializedName("appId")
        var appId: String = "",

        @SerializedName("appName")
        var appName: String = "",

        @SerializedName("Ratings")
        var ratings: Ratings = Ratings()

): Parcelable