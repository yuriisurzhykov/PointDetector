package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.swipe.SwipeAction
import com.yuriisurzhykov.swipe.SwipeBehavior
import com.yuriisurzhykov.swipe.SwipeSide

/**
 * What can be done to a point of the list by swiping it. The swipe itself is done by the swipe
 * engine, here is only what the swipes mean for the points.
 */
class PointSwipeActions(
    private val context: Context,
    private val vibration: IVibrationService,
    private val adapter: PointsListAdapter
) {

    /**
     * Swipe to the right switches the favorite of the point, the point goes back to its place.
     */
    fun favorite(onSwiped: (PointUi) -> Unit): SwipeAction = SwipeAction.Base(
        side = SwipeSide.RIGHT,
        behavior = SwipeBehavior.RETURN,
        color = context.getColor(R.color.favorite_background_color),
        threshold = context.resources.getDimensionPixelSize(R.dimen.favorite_max_swipe_offset),
        icon = icon(R.drawable.ic_favorite),
        available = { point(it) != null },
        reached = { vibration.vibrate(context) },
        swiped = { holder -> point(holder)?.let(onSwiped) }
    )

    /**
     * Swipe to the left removes the point that the user has added. The points of the cloud
     * can't be removed, so they can't be swiped to the left.
     */
    fun deleteUserPoint(onSwiped: (PointUi) -> Unit): SwipeAction =
        remove(isRemovable = { it.isUserPoint }, onSwiped = onSwiped)

    /**
     * Swipe to the left removes the point from the favorites and from the list.
     */
    fun unfavorite(onSwiped: (PointUi) -> Unit): SwipeAction =
        remove(isRemovable = { true }, onSwiped = onSwiped)

    private fun remove(
        isRemovable: (PointUi) -> Boolean,
        onSwiped: (PointUi) -> Unit
    ): SwipeAction = SwipeAction.Base(
        side = SwipeSide.LEFT,
        behavior = SwipeBehavior.DISMISS,
        color = context.getColor(R.color.recycler_view_background_deletion_color),
        threshold = context.resources.displayMetrics.widthPixels / 2,
        icon = icon(R.drawable.ic_delete),
        available = { holder -> point(holder)?.let(isRemovable) == true },
        reached = { vibration.vibrate(context) },
        swiped = { holder ->
            val point = point(holder)
            if (point != null) {
                adapter.removeItem(holder.bindingAdapterPosition)
                onSwiped(point)
            }
        }
    )

    private fun point(holder: RecyclerView.ViewHolder): PointUi? {
        return adapter.pointAt(holder.bindingAdapterPosition)
    }

    private fun icon(@DrawableRes drawableId: Int) =
        ContextCompat.getDrawable(context, drawableId)
            ?.mutate()
            ?.apply { setTint(context.getColor(R.color.white)) }
}
