package com.yuriisurzhykov.pointdetector.presentation.city

import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi

/**
 * The next state of the list from the current one and an answer. [answer] is the list the answer
 * carries, or null when it carries none (no network, an error). Pure, so it is tested alone.
 */
object CityListReducer {

    fun reduce(current: CityListState, answer: List<CityUi>?): CityListState = when {
        !answer.isNullOrEmpty() -> CityListState.Loaded(answer)
        current is CityListState.Loaded -> current
        else -> CityListState.Failed
    }
}
