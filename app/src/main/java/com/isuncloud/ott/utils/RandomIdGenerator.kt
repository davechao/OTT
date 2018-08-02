package com.isuncloud.isuntvmall.utils

import java.util.*

object RandomIDGenerator {

    fun generated(): String {
        return UUID.randomUUID().toString()
    }
}