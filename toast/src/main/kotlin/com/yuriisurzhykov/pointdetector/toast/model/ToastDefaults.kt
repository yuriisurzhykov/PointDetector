package com.yuriisurzhykov.pointdetector.toast.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/** Policy values of the toast queue, named once so no call site carries a literal. */
object ToastDefaults {
    val TimedDuration: Duration = 3.seconds
}
