package com.yuriisurzhykov.pointdetector.toast

import com.yuriisurzhykov.pointdetector.toast.model.ToastData

internal data class ToastSnapshot(
    val active: ToastTicket? = null,
    val pending: List<ToastTicket> = emptyList(),
) {

    val activeToast: ToastData? get() = active?.data

    fun promoteNext(vararg outgoingEffects: ToastSideEffect): ToastTransition {
        val next = pending.firstOrNull()
        val remaining = if (next != null) pending.drop(1) else emptyList()
        return ToastTransition(
            snapshot = ToastSnapshot(active = next, pending = remaining),
            effects = buildList {
                add(ToastSideEffect.CancelTimer)
                addAll(outgoingEffects)
                next?.let { addAll(it.timerEffects()) }
            },
        )
    }

    companion object {
        val Empty = ToastSnapshot()
    }
}
