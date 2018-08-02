package com.isuncloud.isuntvmall.utils

import android.util.Log.ERROR
import android.util.Log.WARN
import timber.log.Timber

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            ERROR, WARN -> {

            }
        }
    }
}