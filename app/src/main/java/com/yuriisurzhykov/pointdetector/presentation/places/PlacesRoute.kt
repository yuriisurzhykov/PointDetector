package com.yuriisurzhykov.pointdetector.presentation.places

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.add.AddPlaceActions
import com.yuriisurzhykov.pointdetector.presentation.add.AddPlaceScreen
import com.yuriisurzhykov.pointdetector.presentation.add.AddPlaceViewModel
import com.yuriisurzhykov.pointdetector.presentation.add.FactAnswer
import com.yuriisurzhykov.pointdetector.presentation.city.CityPickerActions
import com.yuriisurzhykov.pointdetector.presentation.city.CityPickerSheet
import com.yuriisurzhykov.pointdetector.presentation.city.CityViewModel
import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.filter.FiltersActions
import com.yuriisurzhykov.pointdetector.presentation.filter.FiltersSheet
import com.yuriisurzhykov.pointdetector.presentation.filter.FiltersViewModel
import com.yuriisurzhykov.pointdetector.presentation.main.UiDependencies
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import java.time.DayOfWeek

/**
 * The main screen with what lies over it: the city and filters sheets and the flow of adding a
 * place. It connects the view models to the screens; opening a place is left to the caller.
 */
@Composable
fun PlacesRoute(
    ui: UiDependencies,
    onOpenPlace: (PointUi) -> Unit,
    places: PlacesViewModel = hiltViewModel(),
    cities: CityViewModel = hiltViewModel(),
    filters: FiltersViewModel = hiltViewModel(),
    add: AddPlaceViewModel = hiltViewModel(),
) {
    val state by places.state.collectAsStateWithLifecycle()
    val actions = remember(places, cities, filters, add, onOpenPlace) {
        placesActions(places, cities, filters, add, onOpenPlace)
    }
    val addActions = remember(places, add) { addPlaceActions(places, add) }
    val cityActions = remember(places, cities) { cityActions(cities) { places.onOverlay(PlacesOverlay.None) } }
    val addCityActions = remember(cities, add) { cityActions(cities) { add.onCitySheet(false) } }
    val automatic by cities.automatic.collectAsStateWithLifecycle()
    val filtersActions = remember(places, filters) { filtersActions(filters) { places.onOverlay(PlacesOverlay.None) } }

    // The chip and the list follow the selected city: a new city clears the list and loads its places.
    val selectedCity by cities.selected.collectAsStateWithLifecycle()
    LaunchedEffect(selectedCity) { places.onCityChanged(selectedCity) }

    // The data is brought up to date every time the app comes to the front.
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        cities.sync()
        places.reload()
    }

    PlacesScreen(state, actions, ui.mapFactory, ui.cards, ui.statuses)
    BackHandler(enabled = state.overlay != PlacesOverlay.None) { places.onOverlay(PlacesOverlay.None) }
    when (state.overlay) {
        PlacesOverlay.City -> {
            val list by cities.list.collectAsStateWithLifecycle()
            val selected by cities.selected.collectAsStateWithLifecycle()
            val failed by cities.detectionFailed.collectAsStateWithLifecycle()
            CityPickerSheet(list, selected?.code, automatic, failed, cityActions)
        }

        PlacesOverlay.Filters -> {
            val filtersState by filters.state.collectAsStateWithLifecycle()
            FiltersSheet(filtersState, filtersActions) {
                places.onOverlay(PlacesOverlay.None)
                places.reload()
            }
        }

        PlacesOverlay.AddPlace -> {
            val addState by add.state.collectAsStateWithLifecycle()
            val list by cities.list.collectAsStateWithLifecycle()
            val selected by cities.selected.collectAsStateWithLifecycle()
            val failed by cities.detectionFailed.collectAsStateWithLifecycle()
            BackHandler { addActions.onBack() }
            AddPlaceScreen(addState, selected, ui.addMapper, addActions)
            if (addState.citySheet) CityPickerSheet(list, selected?.code, automatic, failed, addCityActions)
        }

        PlacesOverlay.None -> Unit
    }
}

private fun placesActions(
    places: PlacesViewModel,
    cities: CityViewModel,
    filters: FiltersViewModel,
    add: AddPlaceViewModel,
    onOpenPlace: (PointUi) -> Unit,
) = object : PlacesActions {
    override fun onOpenPlace(point: PointUi) {
        places.onSelect(point.id, bringIntoView = false)
        onOpenPlace(point)
    }
    override fun onFavorite(point: PointUi) = places.onFavorite(point)
    override fun onSelect(pointId: Long) = places.onSelect(pointId)
    override fun onTab(tab: PlacesTab) = places.onTab(tab)
    override fun onQuery(text: String) = places.onQuery(text)
    override fun onSearching(searching: Boolean) = places.onSearching(searching)
    override fun onSheet(state: PdSheetState) = places.onSheet(state)
    override fun onLocate() = places.onLocate()

    override fun onFilters() {
        filters.load()
        places.onOverlay(PlacesOverlay.Filters)
    }

    override fun onCity() {
        cities.sync()
        places.onOverlay(PlacesOverlay.City)
    }

    override fun onAdd() {
        add.onAnother()
        places.onOverlay(PlacesOverlay.AddPlace)
    }
}

private fun cityActions(cities: CityViewModel, close: () -> Unit) = object : CityPickerActions {
    override fun onSelect(city: CityUi) = cities.select(city)
    override fun onDetect() = cities.detect(onDetected = close)
    override fun onRetry() = cities.retry()
    override fun onClose() = close()
}

private fun filtersActions(filters: FiltersViewModel, close: () -> Unit) = object : FiltersActions {
    override fun onSort(id: String) = filters.selectSort(id)
    override fun onUnit(id: String) = filters.selectUnit(id)
    override fun onReset() = filters.reset()
    override fun onApply() = filters.apply()
    override fun onClose() = close()
}

private fun addPlaceActions(places: PlacesViewModel, add: AddPlaceViewModel) = object : AddPlaceActions {
    override fun onBack() {
        if (!add.onBack()) places.onOverlay(PlacesOverlay.None)
    }

    override fun onQuery(text: String) = add.onQuery(text)
    override fun onPick(point: Point) = add.onPick(point)
    override fun onManual() = add.onManual()
    override fun onName(text: String) = add.onName(text)
    override fun onAddress(text: String) = add.onAddress(text)
    override fun onAnswer(type: String, answer: FactAnswer) = add.onAnswer(type, answer)
    override fun onCity() = add.onCitySheet(true)
    override fun onOpenHours() = add.onOpenHours()
    override fun onCloseHours() = add.onCloseHours()
    override fun onDay(day: DayOfWeek) = add.onDay(day)
    override fun onFrom(digits: String) = add.onFrom(digits)
    override fun onTo(digits: String) = add.onTo(digits)
    override fun onAddHours() = add.onAddHours()
    override fun onRemoveHours(index: Int) = add.onRemoveHours(index)
    override fun onSave() = add.onSave()
    override fun onAnother() = add.onAnother()

    override fun onShowOnMap() {
        val saved = add.state.value.saved ?: return places.onOverlay(PlacesOverlay.None)
        places.onShowPlace(saved.latitude, saved.longitude)
    }
}
