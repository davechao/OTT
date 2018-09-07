package com.isuncloud.ott.repository.model.rn

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventRequest(

        @SerializedName("eventID")
        var eventID: String = "",

        @SerializedName("request")
        var request: BaseModel = BaseModel()

): Parcelable