package com.yuriisurzhykov.pointdetector.presentation.favorites

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractSwipeCallback
import com.yuriisurzhykov.pointdetector.uicomponents.list.BaseRecyclerViewAdapter

class BothSideSwipeCallback(
    private val rightSwipeCallback: AbstractSwipeCallback,
    private val leftSwipeCallback: AbstractSwipeCallback,
    adapter: BaseRecyclerViewAdapter
) : AbstractSwipeCallback(adapter) {

    private var swipeDirection = 0
    override fun makeSideMovementFlag(): Int {
        return rightSwipeCallback.makeSideMovementFlag() or leftSwipeCallback.makeSideMovementFlag()
    }

    override fun makeSwipeColor(swipeDirection: Int): Int {
        this.swipeDirection = swipeDirection
        return if (swipeDirection == ItemTouchHelper.RIGHT) {
            rightSwipeCallback.makeSwipeColor(swipeDirection)
        } else {
            leftSwipeCallback.makeSwipeColor(swipeDirection)
        }
    }

    override fun makeBounds(itemView: View, swipeDirection: Int, dX: Float, dY: Float): Rect {
        return if (swipeDirection == ItemTouchHelper.RIGHT) {
            rightSwipeCallback.makeBounds(itemView, swipeDirection, dX, dY)
        } else {
            leftSwipeCallback.makeBounds(itemView, swipeDirection, dX, dY)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT) {
            rightSwipeCallback.onSwiped(viewHolder, direction)
        } else {
            leftSwipeCallback.onSwiped(viewHolder, direction)
        }
    }

    override fun onChildDraw(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (dX > 0) {
            rightSwipeCallback.onChildDraw(
                canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
            )
        } else {
            leftSwipeCallback.onChildDraw(
                canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
            )
        }
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        if (swipeDirection == ItemTouchHelper.RIGHT) {
            return rightSwipeCallback.getSwipeVelocityThreshold(defaultValue)
        }
        return super.getSwipeVelocityThreshold(defaultValue)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        if (swipeDirection == ItemTouchHelper.RIGHT) {
            return rightSwipeCallback.getSwipeEscapeVelocity(defaultValue)
        }
        return super.getSwipeEscapeVelocity(defaultValue)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        if (swipeDirection == ItemTouchHelper.RIGHT) {
            return rightSwipeCallback.getSwipeThreshold(viewHolder)
        }
        return super.getSwipeThreshold(viewHolder)
    }
}