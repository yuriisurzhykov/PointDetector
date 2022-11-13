package com.yuriisurzhykov.pointdetector.presentation.delegate

import android.os.Looper

fun checkMainThread() {
    check(Looper.getMainLooper() === Looper.myLooper()) {
        "The method must be called on the main thread"
    }
}