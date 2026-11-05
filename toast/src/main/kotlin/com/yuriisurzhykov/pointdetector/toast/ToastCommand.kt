package com.yuriisurzhykov.pointdetector.toast

import com.yuriisurzhykov.pointdetector.toast.model.ToastResult

/** Everything that can happen to the queue. Each command is a pure function of the snapshot. */
internal sealed interface ToastCommand {

    fun reduce(snapshot: ToastSnapshot): ToastTransition

    data class Enqueue(val ticket: ToastTicket) : ToastCommand {

        override fun reduce(snapshot: ToastSnapshot): ToastTransition {
            val active = snapshot.active
                ?: return ToastTransition(
                    snapshot = ToastSnapshot(active = ticket, pending = snapshot.pending),
                    effects = ticket.timerEffects(),
                )

            if (ticket.data.type.priority > active.data.type.priority) {
                return ToastTransition(
                    snapshot = ToastSnapshot(active = ticket, pending = snapshot.pending),
                    effects = buildList {
                        add(ToastSideEffect.CancelTimer)
                        add(ToastSideEffect.Complete(active, ToastResult.Replaced))
                        addAll(ticket.timerEffects())
                    },
                )
            }

            return ToastTransition(snapshot = snapshot.copy(pending = snapshot.pending + ticket))
        }
    }

    data class Dismiss(val id: String?) : ToastCommand {

        override fun reduce(snapshot: ToastSnapshot): ToastTransition {
            if (id != null) {
                val queued = snapshot.pending.firstOrNull { it.id == id }
                if (queued != null) {
                    return ToastTransition(
                        snapshot = snapshot.copy(pending = snapshot.pending - queued),
                        effects = listOf(ToastSideEffect.Complete(queued, ToastResult.Dismissed)),
                    )
                }
            }

            val active = snapshot.active ?: return ToastTransition(snapshot)
            if (id != null && active.id != id) return ToastTransition(snapshot)

            return snapshot.promoteNext(ToastSideEffect.Complete(active, ToastResult.Dismissed))
        }
    }

    data class TimerExpired(val ticketId: String) : ToastCommand {

        override fun reduce(snapshot: ToastSnapshot): ToastTransition {
            val active = snapshot.active ?: return ToastTransition(snapshot)
            if (active.id != ticketId) return ToastTransition(snapshot)

            return snapshot.promoteNext(ToastSideEffect.Complete(active, ToastResult.TimedOut))
        }
    }

    data class Evict(val ticket: ToastTicket) : ToastCommand {

        override fun reduce(snapshot: ToastSnapshot): ToastTransition {
            if (snapshot.active === ticket) {
                return snapshot.promoteNext(ToastSideEffect.CancelTicket(ticket))
            }
            if (ticket in snapshot.pending) {
                return ToastTransition(
                    snapshot = snapshot.copy(pending = snapshot.pending - ticket),
                    effects = listOf(ToastSideEffect.CancelTicket(ticket)),
                )
            }
            return ToastTransition(snapshot)
        }
    }
}
