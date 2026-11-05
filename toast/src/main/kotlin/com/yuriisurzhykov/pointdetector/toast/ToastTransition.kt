package com.yuriisurzhykov.pointdetector.toast

internal data class ToastTransition(
    val snapshot: ToastSnapshot,
    val effects: List<ToastSideEffect> = emptyList(),
)
