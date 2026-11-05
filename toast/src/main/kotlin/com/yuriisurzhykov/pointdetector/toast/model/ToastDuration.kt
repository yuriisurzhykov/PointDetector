package com.yuriisurzhykov.pointdetector.toast.model

import kotlin.time.Duration

sealed interface ToastDuration {

    data object Indefinite : ToastDuration

    data class Timed(val duration: Duration = ToastDefaults.TimedDuration) : ToastDuration
}
