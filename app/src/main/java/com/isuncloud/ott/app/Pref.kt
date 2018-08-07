package com.isuncloud.ott.app

import android.content.Context
import com.google.gson.Gson

class Pref(private val gson: Gson, context: Context, preferenceFileName: String)
    : AbstractPref(context, preferenceFileName) {

}
