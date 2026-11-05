package com.yuriisurzhykov.pointdetector.toast

import com.yuriisurzhykov.pointdetector.toast.model.ToastResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

internal class EffectExecutor(
    private val scope: CoroutineScope,
    private val commands: SendChannel<ToastCommand>,
) {
    private var timerJob: Job? = null

    fun completeTicket(ticket: ToastTicket, result: ToastResult) = ticket.complete(result)

    fun cancelTicket(ticket: ToastTicket) = ticket.cancel()

    fun scheduleTimer(ticketId: String, duration: Duration) {
        timerJob?.cancel()
        timerJob = scope.launch {
            delay(duration)
            commands.send(ToastCommand.TimerExpired(ticketId))
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}
