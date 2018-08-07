package com.isuncloud.ott.repository.model

data class SignItem(
        val hashData: ByteArray,
        val sig: Sig
)