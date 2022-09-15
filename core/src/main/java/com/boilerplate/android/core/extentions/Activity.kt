package com.boilerplate.android.core.extentions

import android.app.Activity
import android.os.Build

@Suppress("DEPRECATION")
fun Activity.getDeviceRotation(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        display?.rotation ?: 0
    } else {
        windowManager.defaultDisplay.rotation
    }
}