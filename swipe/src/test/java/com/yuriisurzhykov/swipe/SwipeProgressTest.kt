package com.yuriisurzhykov.swipe

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SwipeProgressTest {

    private lateinit var progress: SwipeProgress

    @Before
    fun `init resources for test`() {
        progress = SwipeProgress()
    }

    @Test
    fun `test item dragged to the threshold and released is released at that side`() {
        progress.moved(SwipeSide.RIGHT, 40f, 100)
        progress.moved(SwipeSide.RIGHT, 120f, 100)

        assertEquals(SwipeSide.RIGHT, progress.released())
    }

    @Test
    fun `test item not dragged to the threshold is released as nothing`() {
        progress.moved(SwipeSide.RIGHT, 99f, 100)

        assertNull(progress.released())
    }

    @Test
    fun `test item dragged to the threshold and brought back is released as nothing`() {
        progress.moved(SwipeSide.RIGHT, 150f, 100)
        progress.moved(SwipeSide.RIGHT, 30f, 100)

        assertNull(progress.released())
    }

    @Test
    fun `test threshold is reached once while the item stays beyond it`() {
        assertFalse(progress.moved(SwipeSide.RIGHT, 50f, 100))
        assertTrue(progress.moved(SwipeSide.RIGHT, 100f, 100))
        assertFalse(progress.moved(SwipeSide.RIGHT, 140f, 100))
        assertFalse(progress.moved(SwipeSide.RIGHT, 200f, 100))
    }

    @Test
    fun `test threshold is reached again after the item was brought back`() {
        assertTrue(progress.moved(SwipeSide.RIGHT, 120f, 100))
        assertFalse(progress.moved(SwipeSide.RIGHT, 60f, 100))
        assertTrue(progress.moved(SwipeSide.RIGHT, 120f, 100))
    }

    @Test
    fun `test swiping to the other side reaches its own threshold`() {
        assertTrue(progress.moved(SwipeSide.RIGHT, 120f, 100))
        assertTrue(progress.moved(SwipeSide.LEFT, 120f, 100))

        assertEquals(SwipeSide.LEFT, progress.released())
    }

    @Test
    fun `test release is given once`() {
        progress.moved(SwipeSide.RIGHT, 120f, 100)

        assertEquals(SwipeSide.RIGHT, progress.released())
        assertNull(progress.released())
    }

    @Test
    fun `test new drag does not remember the previous one`() {
        progress.moved(SwipeSide.RIGHT, 120f, 100)

        progress.start()

        assertNull(progress.released())
    }
}
