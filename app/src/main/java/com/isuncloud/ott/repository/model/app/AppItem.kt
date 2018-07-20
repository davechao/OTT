package com.isuncloud.ott.repository.model.app

import android.graphics.drawable.Drawable

data class AppItem(
    val appId: String = "",
    val appName: String = "",
    val icon: Drawable
)