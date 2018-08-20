package com.isuncloud.ott.repository.model.rn

import com.google.gson.annotations.SerializedName

data class MakeLightTx(

        @SerializedName("from")
        var fromAddress: String = "",

        @SerializedName("to")
        var toAddress: String = "",

        @SerializedName("value")
        var value: String = "0",

        @SerializedName("assetID")
        var assetID: String = "0",

        @SerializedName("fee")
        var fee: String = "0",

        @SerializedName("metadata")
        var metadata: MetaDataModel = MetaDataModel()

): BaseModel()