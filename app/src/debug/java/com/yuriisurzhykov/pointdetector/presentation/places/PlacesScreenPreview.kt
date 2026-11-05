package com.yuriisurzhykov.pointdetector.presentation.places

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.places.help.AppointmentFactFactory
import com.yuriisurzhykov.pointdetector.presentation.places.help.DocumentsFactFactory
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdSketchMapFactory
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay
import java.time.DayOfWeek

private val MONDAY_NOON = PlacesClock(DayOfWeek.MONDAY, 12 * 60)

private val NO_ACTIONS = object : PlacesActions {
    override fun onOpenPlace(point: PointUi) = Unit
    override fun onFavorite(point: PointUi) = Unit
    override fun onSelect(pointId: Long) = Unit
    override fun onTab(tab: PlacesTab) = Unit
    override fun onQuery(text: String) = Unit
    override fun onSearching(searching: Boolean) = Unit
    override fun onSheet(state: PdSheetState) = Unit
    override fun onLocate() = Unit
    override fun onFilters() = Unit
    override fun onCity() = Unit
    override fun onAdd() = Unit
}

private fun place(id: Long, name: String, lat: Double, lng: Double, hours: List<WeekDay>, help: Map<String, Boolean> = emptyMap()) =
    PointUi(
        address = "${2000 + id * 110} Capitol Ave",
        coordinates = LatLng(lat, lng),
        distanceString = "${id}.${id} km",
        distanceDouble = id.toDouble(),
        placeName = name,
        workingHours = hours,
        favoriteState = FavoriteState.Factory.build(id == 3L),
        id = id,
        help = help,
    )

private val SAMPLE = listOf(
    place(1, "Midtown Community Pantry", 38.575, -121.470, listOf(WeekDay("Mon", 1, "09:00", "17:00")), mapOf("appointment" to false, "documents" to false)),
    place(2, "Loaves & Fishes Kitchen", 38.590, -121.495, listOf(WeekDay("Mon", 1, "07:30", "12:30"))),
    place(3, "River City Food Share", 38.560, -121.440, listOf(WeekDay("Tue", 2, "10:00", "19:00")), mapOf("appointment" to false, "documents" to true)),
    place(4, "Oak Park Pantry", 38.540, -121.470, emptyList(), mapOf("appointment" to true)),
)

private val STATE = PlacesUiState(
    places = SAMPLE,
    isLoading = false,
    sheet = PdSheetState.Half,
    user = UserSpot(38.568, -121.480),
    selectedId = 1,
)

private val CARDS = PlaceCardMapper(
    PlaceStatusMapper(),
    PlaceTagsMapper(setOf(AppointmentFactFactory(), DocumentsFactFactory())),
)

@Composable
private fun Screen(state: PlacesUiState) {
    PdTheme {
        PlacesScreen(state, NO_ACTIONS, PdSketchMapFactory, CARDS, PlaceStatusMapper(), MONDAY_NOON)
    }
}

@Preview(name = "Light", widthDp = 360, heightDp = 720)
@Preview(name = "Dark", widthDp = 360, heightDp = 720, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlacesScreenPreview() = Screen(STATE)

@Preview(name = "Peek", widthDp = 360, heightDp = 720)
@Composable
private fun PlacesScreenPeekPreview() = Screen(STATE.copy(sheet = PdSheetState.Peek))

@Preview(name = "Loading", widthDp = 360, heightDp = 720)
@Composable
private fun PlacesScreenLoadingPreview() = Screen(PlacesUiState(isLoading = true, sheet = PdSheetState.Half))

@Preview(name = "Empty city", widthDp = 360, heightDp = 720)
@Composable
private fun PlacesScreenEmptyPreview() = Screen(PlacesUiState(isLoading = false, sheet = PdSheetState.Half))

@Preview(name = "Favorites tab", widthDp = 360, heightDp = 720)
@Composable
private fun PlacesScreenFavoritesPreview() = Screen(STATE.copy(tab = PlacesTab.Favorites))
