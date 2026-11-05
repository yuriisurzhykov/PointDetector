package com.yuriisurzhykov.pointdetector.domain.messages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppMessagesTest {

    private var time = 0L
    private val messages = AppMessages { time }
    private val received = mutableListOf<AppMessage>()

    init {
        CoroutineScope(UnconfinedTestDispatcher()).launch { messages.events.collect { received += it } }
    }

    @Test
    fun `test a message reaches who listens`() {
        messages.post(AppMessage.FavoriteAdded)

        assertEquals(listOf<AppMessage>(AppMessage.FavoriteAdded), received)
    }

    @Test
    fun `test the same message is not repeated while it is quiet`() {
        messages.post(AppMessage.FavoriteAdded)
        time = 500
        messages.post(AppMessage.FavoriteAdded)

        assertEquals(1, received.size)
    }

    @Test
    fun `test the same message is posted again after the quiet time`() {
        messages.post(AppMessage.FavoriteAdded)
        time = 1_500
        messages.post(AppMessage.FavoriteAdded)

        assertEquals(2, received.size)
    }

    @Test
    fun `test an offline phone is told once in a long while, not at every reload`() {
        messages.post(AppMessage.NoConnection)
        time = 20_000
        messages.post(AppMessage.NoConnection)
        time = 31_000
        messages.post(AppMessage.NoConnection)

        assertEquals(2, received.size)
    }

    @Test
    fun `test different messages do not hide each other`() {
        messages.post(AppMessage.FavoriteAdded)
        messages.post(AppMessage.FavoriteRemoved)
        messages.post(AppMessage.PlaceAdded("A"))
        messages.post(AppMessage.PlaceAdded("B"))

        assertEquals(4, received.size)
    }

    @Test
    fun `test the heart is told by what it was before the tap`() {
        messages.postFavoriteChange(wasFavorite = false)
        time = 5_000
        messages.postFavoriteChange(wasFavorite = true)

        assertEquals(listOf<AppMessage>(AppMessage.FavoriteAdded, AppMessage.FavoriteRemoved), received)
    }
}
