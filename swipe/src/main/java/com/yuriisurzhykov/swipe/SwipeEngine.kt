package com.yuriisurzhykov.swipe

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Swipes of the items of any list: give it the actions and attach it to the list, it takes care
 * of the movement, the background, the way back to the place and the removal. It doesn't depend on
 * the adapter or on what the list shows.
 */
interface SwipeEngine {

    fun attachTo(recyclerView: RecyclerView)

    /**
     * There is one action for a side for now.
     */
    class Base(private val actions: List<SwipeAction>) : SwipeEngine {

        init {
            require(actions.map { it.side }.distinct().size == actions.size) {
                "There can be only one swipe action for a side, got: ${actions.map { it.side }}"
            }
        }

        override fun attachTo(recyclerView: RecyclerView) {
            ItemTouchHelper(SwipeCallback(actions)).attachToRecyclerView(recyclerView)
        }
    }
}
