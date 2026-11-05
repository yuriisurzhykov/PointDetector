package com.yuriisurzhykov.pointdetector.presentation.messages

import com.yuriisurzhykov.pointdetector.domain.messages.AppMessage
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessage.PlaceNotSaved.Reason
import com.yuriisurzhykov.pointdetector.toast.model.ToastDuration
import com.yuriisurzhykov.pointdetector.toast.model.ToastType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ToastMessagesTest {

    private val toasts = ToastMessages()

    private fun typeOf(message: AppMessage): ToastType = toasts.toast(message).type

    @Test
    fun `test the lack of a network and a failed update are warnings`() {
        assertEquals(ToastType.Warning, typeOf(AppMessage.NoConnection))
        assertEquals(ToastType.Warning, typeOf(AppMessage.SyncFailed))
    }

    @Test
    fun `test an added place is a success`() {
        assertEquals(ToastType.Success, typeOf(AppMessage.PlaceAdded("Pantry")))
    }

    @Test
    fun `test a place that was not saved is an error whatever the reason`() {
        Reason.entries.forEach { reason ->
            assertEquals(ToastType.Error, typeOf(AppMessage.PlaceNotSaved(reason)))
        }
    }

    @Test
    fun `test a deleted place is a success and one that stayed is an error`() {
        assertEquals(ToastType.Success, typeOf(AppMessage.PlaceDeleted))
        assertEquals(ToastType.Error, typeOf(AppMessage.PlaceNotDeleted))
    }

    @Test
    fun `test the heart and a city that was not found are quiet`() {
        assertEquals(ToastType.Info, typeOf(AppMessage.FavoriteAdded))
        assertEquals(ToastType.Info, typeOf(AppMessage.FavoriteRemoved))
        assertEquals(ToastType.Warning, typeOf(AppMessage.CityNotDetected))
    }

    @Test
    fun `test no toast stays until it is swiped away`() {
        val messages = listOf(
            AppMessage.NoConnection, AppMessage.SyncFailed, AppMessage.PlaceAdded("A"),
            AppMessage.PlaceNotSaved(Reason.Failed), AppMessage.FavoriteAdded, AppMessage.FavoriteRemoved,
            AppMessage.CityNotDetected, AppMessage.PlaceDeleted, AppMessage.PlaceNotDeleted,
        )

        messages.forEach { assertTrue("$it", toasts.toast(it).duration is ToastDuration.Timed) }
    }

    @Test
    fun `test the heart is shown shorter than a problem`() {
        val heart = toasts.toast(AppMessage.FavoriteAdded).duration as ToastDuration.Timed
        val problem = toasts.toast(AppMessage.NoConnection).duration as ToastDuration.Timed

        assertTrue(heart.duration < problem.duration)
    }
}
