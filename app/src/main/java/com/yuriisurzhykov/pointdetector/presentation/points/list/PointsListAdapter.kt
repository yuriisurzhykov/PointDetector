package com.yuriisurzhykov.pointdetector.presentation.points.list

import com.yuriisurzhykov.pointdetector.presentation.favorites.FavoritesApply
import com.yuriisurzhykov.pointdetector.presentation.favorites.IFavoriteAdapter
import com.yuriisurzhykov.pointdetector.presentation.list.BaseViewHolderTypeManager
import com.yuriisurzhykov.pointdetector.uicomponents.list.SwipeViewRecyclerAdapter
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem

class PointsListAdapter : SwipeViewRecyclerAdapter(BaseViewHolderTypeManager()), IFavoriteAdapter {

    var favoritesApply: FavoritesApply<ViewHolderItem>? = null
    override fun markFavorite(position: Int) {
        val item = getItem(position)
        favoritesApply?.applyFavorite(item)
    }
}