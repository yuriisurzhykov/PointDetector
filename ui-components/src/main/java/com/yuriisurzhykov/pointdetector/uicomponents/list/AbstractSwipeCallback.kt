package com.yuriisurzhykov.pointdetector.uicomponents.list

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractSwipeCallback(protected val adapter: BaseRecyclerViewAdapter) :
    ItemTouchHelper.Callback() {

    private val background = ColorDrawable()
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    abstract fun makeSideMovementFlag(): Int

    @ColorInt
    abstract fun makeSwipeColor(swipeDirection: Int): Int

    abstract fun makeBounds(itemView: View, swipeDirection: Int, dX: Float, dY: Float): Rect

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder is SwipableViewHolder && viewHolder.canSwipe()) {
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_SWIPE, makeSideMovementFlag())
        }
        return makeMovementFlags(0, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (viewHolder is SwipableViewHolder && viewHolder.canSwipe()) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                adapter.onStartSwipe(viewHolder)
            } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                adapter.onSwipeReleased(viewHolder)
            }
        }
    }

    override fun onChildDraw(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val isCanceled = dX == 0f && !isCurrentlyActive
        val swipeDirection = if (dX > 0) ItemTouchHelper.RIGHT else ItemTouchHelper.LEFT
        val bounds = makeBounds(itemView, swipeDirection, dX, dY)
        val swipeColor = makeSwipeColor(swipeDirection)

        if (isCanceled) {
            clearCanvas(
                canvas,
                bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat()
            )
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        background.color = swipeColor
        background.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
        background.draw(canvas)
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        canvas.drawRect(left, top, right, bottom, clearPaint)
    }

}