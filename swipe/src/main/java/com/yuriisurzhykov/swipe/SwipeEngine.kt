package com.yuriisurzhykov.swipe

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

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

        /**
         * The action usually changes the item, so the item is bound again while it is still going
         * back to its place. The change animation of the list would start from the place where
         * the item is at that moment and take it back for the second time, so it is turned off.
         */
        override fun attachTo(recyclerView: RecyclerView) {
            (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            ItemTouchHelper(SwipeCallback(actions)).attachToRecyclerView(recyclerView)
        }
    }
}
