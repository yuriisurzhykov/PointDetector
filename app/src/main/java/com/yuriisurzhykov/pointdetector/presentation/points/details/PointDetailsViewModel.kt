package com.yuriisurzhykov.pointdetector.presentation.points.details

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
    private val dispatchers: Dispatchers,
    private val favoritesApply: FavoritesApplyUseCase,
) : ViewModel() {

    private val mutablePoint = MutableStateFlow<PointUi?>(null)

    /** Null until [show] is called; the screen draws the point it was opened with meanwhile. */
    val point: StateFlow<PointUi?> = mutablePoint.asStateFlow()

    /** Takes the place of the screen. A place that is already shown is kept, so a changed heart is not lost. */
    fun show(point: PointUi) {
        mutablePoint.update { current -> current?.takeIf { it.id == point.id } ?: point }
    }

    fun onFavorite() {
        val current = mutablePoint.value ?: return
        dispatchers.launchBackground(viewModelScope) { favoritesApply.applyFavorite(current) }
        mutablePoint.update { it?.toggledFavorite() }
    }
}
