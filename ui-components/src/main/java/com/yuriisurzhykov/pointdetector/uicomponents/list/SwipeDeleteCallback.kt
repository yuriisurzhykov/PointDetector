package com.yuriisurzhykov.pointdetector.uicomponents.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.uicomponents.R

class SwipeDeleteCallback(adapter: BaseRecyclerViewAdapter, context: Context) :
    AbstractSwipeCallback(adapter) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete) as Drawable
    private val intrinsicWidth = deleteIcon.intrinsicWidth
    private val intrinsicHeight = deleteIcon.intrinsicHeight
    private val backgroundColor = context.getColor(R.color.recycler_view_background_deletion_color)

    override fun makeSwipeColor(swipeDirection: Int) = backgroundColor
    override fun makeSideMovementFlag() = ItemTouchHelper.LEFT

    override fun makeBounds(itemView: View, swipeDirection: Int, dX: Float, dY: Float): Rect {
        return Rect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapterPosition = viewHolder.adapterPosition
        adapter.removeItem(viewHolder, adapterPosition)
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
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(canvas)
    }

}