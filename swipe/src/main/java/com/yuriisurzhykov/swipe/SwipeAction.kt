package com.yuriisurzhykov.swipe

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

/**
 * One thing that can be done to a list item by swiping it to a [side]. The action doesn't know
 * what the list shows, it gets the view holder of the swiped item and does the job.
 */
interface SwipeAction {

    val side: SwipeSide

    val behavior: SwipeBehavior

    /**
     * Color of the background that is uncovered while the item is swiped.
     */
    @get:ColorInt
    val color: Int

    /**
     * Drawn on the background next to the edge the swipe started from, can be absent.
     */
    val icon: Drawable?

    /**
     * How far, in pixels, the item must be dragged for the action to be done. For
     * [SwipeBehavior.DISMISS] it is the distance after which the item leaves the list.
     */
    val threshold: Int

    /**
     * Items that don't allow the action (the empty state, the items of another kind) can't be
     * swiped to its side.
     */
    fun isAvailable(viewHolder: RecyclerView.ViewHolder): Boolean

    /**
     * The item has been dragged to the threshold, a good moment for a haptic feedback.
     * It is called once for every time the threshold is reached.
     */
    fun onReached()

    fun onSwiped(viewHolder: RecyclerView.ViewHolder)

    class Base(
        override val side: SwipeSide,
        override val behavior: SwipeBehavior,
        @ColorInt override val color: Int,
        override val threshold: Int,
        override val icon: Drawable? = null,
        private val available: (RecyclerView.ViewHolder) -> Boolean = { true },
        private val reached: () -> Unit = {},
        private val swiped: (RecyclerView.ViewHolder) -> Unit
    ) : SwipeAction {

        override fun isAvailable(viewHolder: RecyclerView.ViewHolder) = available(viewHolder)

        override fun onReached() = reached()

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder) = swiped(viewHolder)
    }
}
