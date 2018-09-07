package com.isuncloud.ott.repository.model

import android.graphics.drawable.Drawable

data class LauncherAppItem(
    val appId: String = "",
    val appName: String = "",
    val icon: Drawable
)