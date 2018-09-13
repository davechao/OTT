package com.isuncloud.ott.repository.model.rn

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MetaDataModel(

        @SerializedName("client")
        var client: Client = Client(),

        @SerializedName("server")
        var server: Server = Server()

): Parcelable