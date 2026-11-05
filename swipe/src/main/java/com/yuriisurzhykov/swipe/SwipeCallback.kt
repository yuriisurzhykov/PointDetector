package com.yuriisurzhykov.swipe

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * The swipe rules of [SwipeEngine] on top of [ItemTouchHelper].
 *
 * [ItemTouchHelper] knows only one kind of a finished swipe: the item is swiped out and the
 * adapter must react. That is what [SwipeBehavior.DISMISS] needs. For [SwipeBehavior.RETURN]
 * the swipe is never finished by the helper (the threshold can't be reached), the item just goes
 * back to its place when the finger is released, and the action is done at that moment if the
 * item was dragged to the threshold.
 */
internal class SwipeCallback(private val actions: List<SwipeAction>) : ItemTouchHelper.Callback() {

    private val background = ColorDrawable()
    private val progress = SwipeProgress()
    private var swipedHolder: RecyclerView.ViewHolder? = null
    private var side: SwipeSide? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = actions
            .filter { it.isAvailable(viewHolder) }
            .fold(0) { flags, action -> flags or action.side.flag }
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun isLongPressDragEnabled() = false

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder != null) {
            swipedHolder = viewHolder
            side = null
            progress.start()
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            release()
        }
    }

    private fun release() {
        val holder = swipedHolder ?: return
        swipedHolder = null
        val action = action(progress.released()) ?: return
        val isHolderInList = holder.bindingAdapterPosition != RecyclerView.NO_POSITION
        if (action.behavior == SwipeBehavior.RETURN && isHolderInList) {
            action.onSwiped(holder)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        action(SwipeSide.of(direction))?.onSwiped(viewHolder)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val action = action(side) ?: return super.getSwipeThreshold(viewHolder)
        return when (action.behavior) {
            SwipeBehavior.RETURN -> UNREACHABLE_THRESHOLD
            SwipeBehavior.DISMISS -> {
                val listWidth = (viewHolder.itemView.parent as? View)?.width
                    ?: viewHolder.itemView.width
                (action.threshold.toFloat() / listWidth.coerceAtLeast(1)).coerceIn(0f, 1f)
            }
        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return if (action(side)?.behavior == SwipeBehavior.RETURN) {
            Float.MAX_VALUE
        } else {
            super.getSwipeEscapeVelocity(defaultValue)
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX != 0f) {
            val current = if (dX > 0) SwipeSide.RIGHT else SwipeSide.LEFT
            side = current
            action(current)?.let { action ->
                draw(canvas, viewHolder.itemView, action, dX)
                if (isCurrentlyActive && progress.moved(current, abs(dX), action.threshold)) {
                    action.onReached()
                }
            }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun draw(canvas: Canvas, itemView: View, action: SwipeAction, dX: Float) {
        val bounds = when (action.side) {
            SwipeSide.RIGHT ->
                Rect(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)

            SwipeSide.LEFT ->
                Rect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        }
        background.color = action.color
        background.bounds = bounds
        background.draw(canvas)
        action.icon?.let { icon ->
            val margin = (itemView.height - icon.intrinsicHeight) / 2
            val left = when (action.side) {
                SwipeSide.RIGHT -> itemView.left + margin
                SwipeSide.LEFT -> itemView.right - margin - icon.intrinsicWidth
            }
            val top = itemView.top + margin
            icon.setBounds(left, top, left + icon.intrinsicWidth, top + icon.intrinsicHeight)
            canvas.save()
            canvas.clipRect(bounds)
            icon.draw(canvas)
            canvas.restore()
        }
    }

    private fun action(side: SwipeSide?) = actions.firstOrNull { it.side == side }

    private companion object {
        /**
         * The helper finishes a swipe when the item is dragged farther than this part of the
         * list width, which can't be done with a finger.
         */
        const val UNREACHABLE_THRESHOLD = 10f
    }
}
