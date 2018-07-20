package com.isuncloud.isuntvmall.utils

import java.util.*

/**
 * Created by derek.chiu on 23/03/2018.
 */

object RandomIDGenerator {

    fun generated(): String {
        return UUID.randomUUID().toString()
    }
}