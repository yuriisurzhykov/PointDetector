package com.yuriisurzhykov.pointdetector.presentation.add

import com.yuriisurzhykov.pointdetector.domain.entities.Point

/** The three steps of adding a place. */
enum class AddStep { Search, Details, Done }

/** What the list under the search field shows. */
sealed interface Suggestions {
    data object Idle : Suggestions
    data object Loading : Suggestions
    data class Found(val points: List<Point>) : Suggestions
    data object NotFound : Suggestions
}

/** Why a place was not saved. */
enum class SaveProblem { CityNotSelected, AddressNotFound, Failed }

/** A saved place, kept for the result screen. */
data class SavedPlace(val name: String, val address: String, val groups: List<HoursGroup>, val latitude: Double, val longitude: Double)

/** Everything the add-a-place flow shows. */
data class AddPlaceState(
    val step: AddStep = AddStep.Search,
    val query: String = "",
    val suggestions: Suggestions = Suggestions.Idle,
    val picked: Point? = null,
    val name: String = "",
    val address: String = "",
    val groups: List<HoursGroup> = emptyList(),
    val answers: Map<String, FactAnswer> = emptyMap(),
    val draft: HoursDraft? = null,
    val citySheet: Boolean = false,
    val hoursError: Boolean = false,
    val problem: SaveProblem? = null,
    val saving: Boolean = false,
    val saved: SavedPlace? = null,
) {
    fun answer(type: String): FactAnswer = answers[type] ?: FactAnswer.Unknown
}
