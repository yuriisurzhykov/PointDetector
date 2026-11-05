package com.yuriisurzhykov.swipe

import androidx.recyclerview.widget.ItemTouchHelper

/**
 * The side an item is swiped to.
 */
enum class SwipeSide(internal val flag: Int) {
    LEFT(ItemTouchHelper.LEFT),
    RIGHT(ItemTouchHelper.RIGHT);

    internal companion object {

        fun of(direction: Int): SwipeSide = entries.first { it.flag == direction }
    }
}
