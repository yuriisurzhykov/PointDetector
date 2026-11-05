package com.yuriisurzhykov.pointdetector.toast

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.toast.model.ToastData

/** What the host shows right now: nothing, or one toast. Either state, never a nullable toast. */
@Immutable
sealed interface ToastSlot {

    data object Empty : ToastSlot

    data class Showing(val data: ToastData) : ToastSlot
}
