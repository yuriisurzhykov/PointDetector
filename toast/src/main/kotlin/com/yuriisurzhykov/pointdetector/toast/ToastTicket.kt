package com.yuriisurzhykov.pointdetector.toast

import com.yuriisurzhykov.pointdetector.toast.model.ToastData
import com.yuriisurzhykov.pointdetector.toast.model.ToastDuration
import com.yuriisurzhykov.pointdetector.toast.model.ToastResult
import kotlinx.coroutines.CompletableDeferred

internal class ToastTicket(val data: ToastData) {

    val id: String get() = data.id

    private val deferred = CompletableDeferred<ToastResult>()

    fun complete(result: ToastResult) {
        deferred.complete(result)
    }

    fun cancel() {
        deferred.cancel()
    }

    suspend fun awaitResult(): ToastResult = deferred.await()

    /** Boundary mapping: public duration to an internal timer effect. */
    fun timerEffects(): List<ToastSideEffect> = when (val d = data.duration) {
        is ToastDuration.Indefinite -> emptyList()
        is ToastDuration.Timed -> listOf(ToastSideEffect.ScheduleTimer(id, d.duration))
    }
}
