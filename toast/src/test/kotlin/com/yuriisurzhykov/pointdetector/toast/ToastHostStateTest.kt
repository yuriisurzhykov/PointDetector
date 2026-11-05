package com.yuriisurzhykov.pointdetector.toast

import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.toast.model.ToastData
import com.yuriisurzhykov.pointdetector.toast.model.ToastDuration
import com.yuriisurzhykov.pointdetector.toast.model.ToastResult
import com.yuriisurzhykov.pointdetector.toast.model.ToastType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ToastHostStateTest {

    private fun TestScope.hostState() = ToastHostState(scope = backgroundScope)

    /**
     * Runs everything that is due. With coroutines-test 1.11 a single `advanceUntilIdle()`
     * can return before the channel-fed pipeline has handled the last command, so the
     * current instant is drained once more.
     */
    private fun TestScope.settle() {
        advanceUntilIdle()
        runCurrent()
        advanceUntilIdle()
    }

    /** The toast on screen, or `null` when the slot is empty. */
    private val ToastHostState.shown: ToastData?
        get() = (slot as? ToastSlot.Showing)?.data

    private fun toast(
        id: String = "test",
        type: ToastType = ToastType.Info,
        duration: ToastDuration = type.defaultDuration,
    ) = ToastData(id = id, type = type, message = PdLiteralText("msg-$id"), duration = duration)

    @Test
    fun initiallyNoToastIsVisible() = runTest {
        val state = hostState()
        settle()
        assertEquals(ToastSlot.Empty, state.slot)
    }

    @Test
    fun showDisplaysToast() = runTest {
        val state = hostState()
        val data = toast(duration = ToastDuration.Indefinite)
        backgroundScope.async { state.show(data) }
        settle()
        assertEquals(data, state.shown)
    }

    @Test
    fun dismissRemovesCurrentToast() = runTest {
        val state = hostState()
        val result = backgroundScope.async { state.show(toast(duration = ToastDuration.Indefinite)) }
        settle()

        state.dismiss()
        settle()

        assertNull(state.shown)
        assertEquals(ToastResult.Dismissed, result.await())
    }

    @Test
    fun dismissByIdRemovesSpecificToast() = runTest {
        val state = hostState()
        val result = backgroundScope.async { state.show(toast(id = "target", duration = ToastDuration.Indefinite)) }
        settle()

        state.dismiss("target")
        settle()

        assertNull(state.shown)
        assertEquals(ToastResult.Dismissed, result.await())
    }

    @Test
    fun dismissByIdIgnoresMismatch() = runTest {
        val state = hostState()
        val data = toast(id = "active", duration = ToastDuration.Indefinite)
        backgroundScope.async { state.show(data) }
        settle()

        state.dismiss("other")
        settle()

        assertEquals(data, state.shown)
    }

    @Test
    fun timedToastAutoDismissesAfterDuration() = runTest {
        val state = hostState()
        val data = toast(duration = ToastDuration.Timed(3.seconds))
        val result = backgroundScope.async { state.show(data) }
        settle()
        assertEquals(data, state.shown)

        advanceTimeBy(3.seconds + 1.seconds / 1000)
        settle()

        assertNull(state.shown)
        assertEquals(ToastResult.TimedOut, result.await())
    }

    @Test
    fun indefiniteToastDoesNotAutoDismiss() = runTest {
        val state = hostState()
        val data = toast(duration = ToastDuration.Indefinite)
        backgroundScope.async { state.show(data) }
        settle()

        advanceTimeBy(60.seconds)
        settle()

        assertEquals(data, state.shown)
    }

    @Test
    fun higherPriorityPreemptsCurrent() = runTest {
        val state = hostState()
        val low = toast(id = "low", type = ToastType.Info, duration = ToastDuration.Indefinite)
        val high = toast(id = "high", type = ToastType.Error, duration = ToastDuration.Indefinite)

        val lowResult = backgroundScope.async { state.show(low) }
        settle()
        assertEquals(low, state.shown)

        backgroundScope.async { state.show(high) }
        settle()

        assertEquals(high, state.shown)
        assertEquals(ToastResult.Replaced, lowResult.await())
    }

    @Test
    fun lowerPriorityIsQueuedFifo() = runTest {
        val state = hostState()
        val high = toast(id = "high", type = ToastType.Error, duration = ToastDuration.Indefinite)
        val low = toast(id = "low", type = ToastType.Info, duration = ToastDuration.Indefinite)

        backgroundScope.async { state.show(high) }
        settle()
        backgroundScope.async { state.show(low) }
        settle()
        assertEquals(high, state.shown)

        state.dismiss()
        settle()
        assertEquals(low, state.shown)
    }

    @Test
    fun fifoOrderIsPreservedForEqualPriority() = runTest {
        val state = hostState()
        val first = toast(id = "first", duration = ToastDuration.Indefinite)
        val second = toast(id = "second", duration = ToastDuration.Indefinite)
        val third = toast(id = "third", duration = ToastDuration.Indefinite)

        backgroundScope.async { state.show(first) }
        settle()
        backgroundScope.async { state.show(second) }
        backgroundScope.async { state.show(third) }
        settle()
        assertEquals(first, state.shown)

        state.dismiss()
        settle()
        assertEquals(second, state.shown)

        state.dismiss()
        settle()
        assertEquals(third, state.shown)
    }

    @Test
    fun dismissQueuedToastByIdRemovesFromQueue() = runTest {
        val state = hostState()
        val active = toast(id = "active", type = ToastType.Error, duration = ToastDuration.Indefinite)
        val queued = toast(id = "queued", duration = ToastDuration.Indefinite)
        val last = toast(id = "last", duration = ToastDuration.Indefinite)

        backgroundScope.async { state.show(active) }
        settle()
        val queuedResult = backgroundScope.async { state.show(queued) }
        backgroundScope.async { state.show(last) }
        settle()

        state.dismiss("queued")
        settle()

        assertEquals(active, state.shown)
        assertEquals(ToastResult.Dismissed, queuedResult.await())

        state.dismiss()
        settle()
        assertEquals(last, state.shown)
    }

    @Test
    fun timedToastPromotesNextFromQueue() = runTest {
        val state = hostState()
        val timed = toast(id = "timed", type = ToastType.Success, duration = ToastDuration.Timed(2.seconds))
        val next = toast(id = "next", duration = ToastDuration.Indefinite)

        backgroundScope.async { state.show(timed) }
        settle()
        backgroundScope.async { state.show(next) }
        settle()
        assertEquals(timed, state.shown)

        advanceTimeBy(2.seconds + 1.seconds / 1000)
        settle()

        assertEquals(next, state.shown)
    }

    @Test
    fun multiplePreemptionsReturnReplaced() = runTest {
        val state = hostState()
        val info = toast(id = "info", type = ToastType.Info, duration = ToastDuration.Indefinite)
        val warning = toast(id = "warning", type = ToastType.Warning, duration = ToastDuration.Indefinite)
        val error = toast(id = "error", type = ToastType.Error, duration = ToastDuration.Indefinite)

        val infoResult = backgroundScope.async { state.show(info) }
        settle()

        val warningResult = backgroundScope.async { state.show(warning) }
        settle()
        assertEquals(ToastResult.Replaced, infoResult.await())

        backgroundScope.async { state.show(error) }
        settle()
        assertEquals(ToastResult.Replaced, warningResult.await())
        assertEquals(error, state.shown)
    }

    @Test
    fun dismissingEveryToastClearsTheSlot() = runTest {
        val state = hostState()
        val first = toast(id = "a", type = ToastType.Error, duration = ToastDuration.Indefinite)
        val second = toast(id = "b", duration = ToastDuration.Indefinite)

        backgroundScope.async { state.show(first) }
        backgroundScope.async { state.show(second) }
        settle()

        state.dismiss("a")
        settle()
        assertTrue(state.shown?.id == "b")

        state.dismiss("b")
        settle()
        assertEquals(ToastSlot.Empty, state.slot)
    }
}
