package com.yuriisurzhykov.pointdetector.presentation.places

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.config.ProjectSortTypeBuilder
import com.yuriisurzhykov.pointdetector.domain.services.LocationManager
import com.yuriisurzhykov.pointdetector.domain.usecase.ConfigUnifiedSource
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesApplyUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAllPointsUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SearchPointUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_MILLIS = 300L
private const val SORT_BY_NAME = "sort_by_name"

/**
 * The state of the main screen: the points of the city, the search, the tab, the selected place
 * and the sheet. It knows nothing about maps or components; the screen turns it into them.
 */
@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val pointsListUseCase: FetchAllPointsUseCase,
    private val searchPointUseCase: SearchPointUseCase,
    private val pointsSorter: ProjectSortTypeBuilder,
    private val favoritesApply: FavoritesApplyUseCase,
    private val config: ConfigUnifiedSource,
) : ViewModel() {

    private val mutableState = MutableStateFlow(PlacesUiState())
    val state: StateFlow<PlacesUiState> = mutableState.asStateFlow()

    private var loadJob: Job? = null
    private var requests = 0

    fun updateUserLocation(location: Location) {
        LocationManager.setLocation(location)
        val first = mutableState.value.user == null
        mutableState.update {
            it.copy(
                user = UserSpot(location.latitude, location.longitude),
                camera = if (first && it.selectedId == null) cameraAt(location.latitude, location.longitude) else it.camera,
            )
        }
        if (mutableState.value.query.isEmpty()) reload()
    }

    fun reload() = startLoading(mutableState.value.query, debounce = false)

    fun onQuery(text: String) {
        mutableState.update { it.copy(query = text) }
        startLoading(text, debounce = true)
    }

    fun onTab(tab: PlacesTab) = mutableState.update { it.copy(tab = tab) }

    fun onSearching(searching: Boolean) {
        mutableState.update { it.copy(searching = searching) }
        if (!searching && mutableState.value.query.isNotEmpty()) onQuery("")
    }

    fun onSheet(sheet: PdSheetState) = mutableState.update { it.copy(sheet = sheet) }

    fun onOverlay(overlay: PlacesOverlay) = mutableState.update { it.copy(overlay = overlay) }

    /** Closes the sheet or the flow that lies over the screen and shows a place on the map, e.g. the one just added. */
    fun onShowPlace(latitude: Double, longitude: Double) {
        mutableState.update { it.copy(overlay = PlacesOverlay.None, camera = cameraAt(latitude, longitude)) }
        reload()
    }

    /**
     * A new city: the points of the old one go away at once and the list shows that it is loading,
     * then the cached points of the new city come and, after the sync, the points of the cloud.
     */
    fun onCityChanged(city: CityUi?) {
        val changed = mutableState.value.city?.code != city?.code
        mutableState.update {
            if (changed) it.copy(city = city, places = emptyList(), isLoading = true, selectedId = null) else it.copy(city = city)
        }
        if (changed) reload()
    }

    /**
     * Selects a place: the camera goes to it and, when [bringIntoView] is set (the pin was tapped on
     * the map), the list brings its card into view. A tap on the card itself must not move the list.
     */
    fun onSelect(pointId: Long, bringIntoView: Boolean = true) {
        val point = mutableState.value.places.firstOrNull { it.id == pointId } ?: return
        mutableState.update {
            it.copy(
                selectedId = pointId,
                camera = cameraAt(point.coordinates.lat, point.coordinates.lng),
                scroll = if (bringIntoView) ScrollRequest(pointId, ++requests) else it.scroll,
            )
        }
    }

    fun onLocate() {
        val user = mutableState.value.user ?: return
        mutableState.update { it.copy(camera = cameraAt(user.latitude, user.longitude)) }
    }

    /** The heart changes at once on the screen and is saved in the background. */
    fun onFavorite(point: PointUi) {
        dispatchers.launchBackground(viewModelScope) { favoritesApply.applyFavorite(point) }
        mutableState.update { current ->
            current.copy(places = current.places.map { if (it.id == point.id) it.toggledFavorite() else it })
        }
    }

    private fun cameraAt(latitude: Double, longitude: Double) = CameraRequest(latitude, longitude, ++requests)

    private fun startLoading(condition: String, debounce: Boolean) {
        loadJob?.cancel()
        loadJob = dispatchers.launchBackground(viewModelScope) {
            if (debounce && condition.isNotEmpty()) delay(SEARCH_DEBOUNCE_MILLIS)
            val points = if (condition.isNotEmpty()) {
                searchPointUseCase.searchPlaceByCondition(condition)
            } else {
                pointsListUseCase.fetchPoints()
            }
            points.collect { publish(it) }
        }
    }

    private suspend fun publish(list: List<PointUi>) {
        val sorted = pointsSorter.performSort(list).toList()
        val byName = config.sortTypeConfigValue() == SORT_BY_NAME
        mutableState.update { current ->
            current.copy(
                places = sorted,
                isLoading = false,
                sortedByName = byName,
                selectedId = current.selectedId?.takeIf { id -> sorted.any { it.id == id } },
            )
        }
    }
}
