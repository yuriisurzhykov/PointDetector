package com.yuriisurzhykov.pointdetector.presentation.points.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesApplyUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.RemoveUserPointUseCase
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessage
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessages
import com.yuriisurzhykov.pointdetector.domain.messages.postFavoriteChange
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
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
    private val messages: AppMessages,
    private val removeUserPoint: RemoveUserPointUseCase,
) : ViewModel() {

    private val mutablePoint = MutableStateFlow<PointUi?>(null)

    /** Null until [show] is called; the screen draws the point it was opened with meanwhile. */
    val point: StateFlow<PointUi?> = mutablePoint.asStateFlow()

    /** Takes the place of the screen. A place that is already shown is kept, so a changed heart is not lost. */
    fun show(point: PointUi) {
        mutablePoint.update { current -> current?.takeIf { it.id == point.id } ?: point }
    }

    /** Deletes the place of the person; [onDeleted] runs on the main thread once it is gone. */
    fun delete(onDeleted: () -> Unit) {
        val current = mutablePoint.value ?: return
        dispatchers.launchBackground(viewModelScope) {
            if (removeUserPoint.remove(current)) {
                messages.post(AppMessage.PlaceDeleted)
                dispatchers.changeToUi { onDeleted() }
            } else {
                messages.post(AppMessage.PlaceNotDeleted)
            }
        }
    }

    fun onFavorite() {
        val current = mutablePoint.value ?: return
        dispatchers.launchBackground(viewModelScope) { favoritesApply.applyFavorite(current) }
        messages.postFavoriteChange(wasFavorite = current.favoriteState == FavoriteState.FavoriteEnabled)
        mutablePoint.update { it?.toggledFavorite() }
    }
}
