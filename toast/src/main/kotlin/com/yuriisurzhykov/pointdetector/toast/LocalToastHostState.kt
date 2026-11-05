package com.yuriisurzhykov.pointdetector.toast

import androidx.compose.runtime.staticCompositionLocalOf

val LocalToastHostState = staticCompositionLocalOf<ToastHostState> {
    error("No ToastHostState provided")
}
