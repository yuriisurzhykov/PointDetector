package com.yuriisurzhykov.pointdetector.presentation.favorites

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService
import com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractSwipeCallback
import com.yuriisurzhykov.pointdetector.uicomponents.list.BaseRecyclerViewAdapter

class FavoriteSwipeCallback(
    adapter: BaseRecyclerViewAdapter,
    context: Context,
    vibration: IVibrationService
) : AbstractSwipeCallback(adapter) {

    private var favoriteIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite) as Drawable
    private val intrinsicWidth = favoriteIcon.intrinsicWidth
    private val intrinsicHeight = favoriteIcon.intrinsicHeight
    private val backgroundColor = context.getColor(R.color.favorite_background_color)
    private val maxFavoriteSwipeThreshold =
        context.resources.getDimensionPixelSize(R.dimen.favorite_max_swipe_offset)
    private val swipeVibrator: SwipeVibrateManager =
        SwipeVibrateManager.Base(vibration, maxFavoriteSwipeThreshold)

    init {
        favoriteIcon.setTint(context.getColor(R.color.white))
    }

    override fun makeSideMovementFlag(): Int = ItemTouchHelper.RIGHT
    override fun makeSwipeColor(swipeDirection: Int) = backgroundColor

    override fun makeBounds(itemView: View, swipeDirection: Int, dX: Float, dY: Float): Rect {
        return Rect(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapterPosition = viewHolder.absoluteAdapterPosition
        if (adapter is IFavoriteAdapter) {
            (adapter as IFavoriteAdapter).markFavorite(adapterPosition)
        }
    }

    override fun onChildDraw(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        /*if (favoriteIcon.intrinsicHeight > viewHolder.itemView.height) {
            val size =
                viewHolder.itemView.height - viewHolder.itemView.paddingTop - viewHolder.itemView.paddingBottom
            favoriteIcon =
                DrawableScale.Base(viewHolder.itemView.context.resources).scale(favoriteIcon, size, size)
        }*/
        swipeVibrator.tryVibrateOnDraw(dX, recyclerView.context)
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Calculate position of delete icon
        val favoriteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val favoriteIconLeft = itemView.left + deleteIconMargin
        val favoriteIconRight = itemView.left + deleteIconMargin + intrinsicWidth
        val favoriteIconBottom = favoriteIconTop + intrinsicHeight

        // Draw the delete icon
        favoriteIcon.setBounds(favoriteIconLeft, favoriteIconTop, favoriteIconRight, favoriteIconBottom)
        favoriteIcon.draw(canvas)
    }

    /**
     * The swipe is done when the item is dragged as far as the vibration says it is, no matter
     * how fast. The speed thresholds are the defaults, so a quick fling works as well.
     */
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val width = viewHolder.itemView.width
        if (width == 0) return super.getSwipeThreshold(viewHolder)
        return (maxFavoriteSwipeThreshold.toFloat() / width).coerceAtMost(1f)
    }
}