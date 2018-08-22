package com.isuncloud.ott.repository.model.rn

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.isuncloud.ott.repository.model.firestore.AppData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Client(

        @SerializedName("appData")
        var appData: AppData = AppData(),

        @SerializedName("deviceId")
        var deviceId: String = ""

): Parcelable