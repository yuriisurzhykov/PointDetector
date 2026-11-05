package com.yuriisurzhykov.pointdetector.toast

import com.yuriisurzhykov.pointdetector.toast.model.ToastResult
import kotlin.time.Duration

internal sealed interface ToastSideEffect {

    fun execute(executor: EffectExecutor)

    data class Complete(val ticket: ToastTicket, val result: ToastResult) : ToastSideEffect {
        override fun execute(executor: EffectExecutor) = executor.completeTicket(ticket, result)
    }

    data class CancelTicket(val ticket: ToastTicket) : ToastSideEffect {
        override fun execute(executor: EffectExecutor) = executor.cancelTicket(ticket)
    }

    data class ScheduleTimer(val ticketId: String, val duration: Duration) : ToastSideEffect {
        override fun execute(executor: EffectExecutor) = executor.scheduleTimer(ticketId, duration)
    }

    data object CancelTimer : ToastSideEffect {
        override fun execute(executor: EffectExecutor) = executor.cancelTimer()
    }
}
