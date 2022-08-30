package com.yuriisurzhykov.pointdetector.presentation.points.create

import com.yuriisurzhykov.pointdetector.core.TAG
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback

open class PointCreateStateHandler {
    fun handleState(state: PointCreateState, navigationCallback: NavigationCallback) {
        when (state) {
            is PointCreateState.PointSelectedState -> {
                handleCreateState(state.point, navigationCallback)
            }
            is PointCreateState.SearchState -> {
                handleSearchState(navigationCallback)
            }
            is PointCreateState.SavedPointState -> {
                handleSaveResult(state, navigationCallback)
            }
        }
    }

    protected open fun handleSaveResult(
        state: PointCreateState.SavedPointState,
        navigationCallback: NavigationCallback
    ) {
        navigationCallback.openFragment(PointCreationResultFragment(), "creation_result")
    }

    protected open fun handleCreateState(point: Point, callback: NavigationCallback) {
        val fragment = PointsAdditionalInfoFragment.newInstance(point)
        callback.openFragment(fragment, fragment.TAG)
    }

    protected open fun handleSearchState(callback: NavigationCallback) {
        val fragment = PointsCreateFragment()
        callback.openFragment(fragment, fragment.TAG)
    }
}