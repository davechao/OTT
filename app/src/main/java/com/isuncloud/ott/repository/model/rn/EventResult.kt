package com.isuncloud.ott.repository.model.rn

import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class EventResult(

        @SerializedName("eventID")
        var eventID: String = "",

        @SerializedName("success")
        var success: Boolean = true,

        @SerializedName("result")
        var result: @RawValue JsonObject = JsonObject()

): Parcelable