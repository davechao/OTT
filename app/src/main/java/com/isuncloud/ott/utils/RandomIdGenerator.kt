package com.isuncloud.ott.utils

import java.util.*

object RandomIDGenerator {

    fun generated(): String {
        return UUID.randomUUID().toString()
    }
}