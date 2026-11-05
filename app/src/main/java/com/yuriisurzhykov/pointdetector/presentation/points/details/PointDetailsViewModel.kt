package com.yuriisurzhykov.pointdetector.presentation.points.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesApplyUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.places.toggledFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** The place of the screen. The heart changes at once and is saved in the background. */
@HiltViewModel
class PointDetailsViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val dispatchers: Dispatchers,
    private val favoritesApply: FavoritesApplyUseCase,
) : ViewModel() {

    private val mutablePoint = MutableStateFlow(
        checkNotNull(savedState.get<PointUi>(ARG_ENTITY)) { "The place screen needs a point in its arguments" },
    )
    val point: StateFlow<PointUi> = mutablePoint.asStateFlow()

    fun onFavorite() {
        val current = mutablePoint.value
        dispatchers.launchBackground(viewModelScope) { favoritesApply.applyFavorite(current) }
        mutablePoint.update { it.toggledFavorite() }
    }

    companion object {
        const val ARG_ENTITY = "argument_name"
    }
}
