package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState

/** Where the person is, as plain numbers: the state does not know about maps. */
data class UserSpot(val latitude: Double, val longitude: Double)

/** An order to move the map camera; a new [token] asks for the same spot again. */
data class CameraRequest(val latitude: Double, val longitude: Double, val token: Int)

/** An order to bring a card into view; a new [token] asks for the same card again. */
data class ScrollRequest(val pointId: Long, val token: Int)

/** Everything the main screen shows. Nullable values stay here, at the boundary of the data. */
data class PlacesUiState(
    val places: List<PointUi> = emptyList(),
    val isLoading: Boolean = true,
    val query: String = "",
    val tab: PlacesTab = PlacesTab.All,
    val selectedId: Long? = null,
    val sheet: PdSheetState = PdSheetState.Peek,
    val searching: Boolean = false,
    val city: CityUi? = null,
    val user: UserSpot? = null,
    val sortedByName: Boolean = false,
    val camera: CameraRequest? = null,
    val scroll: ScrollRequest? = null,
)
