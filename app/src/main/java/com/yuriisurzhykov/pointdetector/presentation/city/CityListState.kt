package com.yuriisurzhykov.pointdetector.presentation.city

import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi

/** What the list of cities is doing: waiting for the first answer, shown, or not available. */
sealed interface CityListState {

    data object Loading : CityListState

    data class Loaded(val cities: List<CityUi>) : CityListState

    data object Failed : CityListState
}
