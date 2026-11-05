package com.yuriisurzhykov.pointdetector.toast

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.yuriisurzhykov.pointdetector.toast.model.ToastData
import com.yuriisurzhykov.pointdetector.toast.model.ToastResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.withContext

/**
 * Priority-aware toast queue exposing the visible toast as observable Compose state.
 *
 * A unidirectional pipeline: `commands -> pure state reduction -> side-effect execution`.
 * Every transition runs on one collecting coroutine, so no mutex is needed.
 *
 * @param scope drives the pipeline and the auto-dismiss timers; a `TestScope` in tests.
 */
@Stable
class ToastHostState(
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob()),
) {

    private val commands = Channel<ToastCommand>(Channel.UNLIMITED)

    var slot: ToastSlot by mutableStateOf(ToastSlot.Empty)
        private set

    init {
        val executor = EffectExecutor(scope, commands)
        commands
            .receiveAsFlow()
            .scan(ToastTransition(ToastSnapshot.Empty)) { (prev, _), command -> command.reduce(prev) }
            .onEach { (snapshot, sideEffects) ->
                sideEffects.forEach { it.execute(executor) }
                slot = snapshot.activeToast?.let { ToastSlot.Showing(it) } ?: ToastSlot.Empty
            }
            .launchIn(scope)
    }

    /**
     * Enqueues or immediately shows [data] and suspends until it is dismissed, times out
     * or is replaced by a toast of higher priority.
     */
    suspend fun show(data: ToastData): ToastResult {
        val ticket = ToastTicket(data)
        commands.send(ToastCommand.Enqueue(ticket))
        return try {
            ticket.awaitResult()
        } finally {
            withContext(NonCancellable) {
                commands.send(ToastCommand.Evict(ticket))
            }
        }
    }

    /** Dismisses the toast with [id], or the visible one when [id] is `null`. */
    fun dismiss(id: String? = null) {
        commands.trySend(ToastCommand.Dismiss(id))
    }
}
