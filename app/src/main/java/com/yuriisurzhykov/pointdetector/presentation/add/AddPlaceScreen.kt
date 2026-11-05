package com.yuriisurzhykov.pointdetector.presentation.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi

/**
 * The flow of adding a place: the page of the current step and, over the form, the sheet for the
 * working hours while one is being typed. The state becomes components here, with `remember`.
 */
@Composable
fun AddPlaceScreen(
    state: AddPlaceState,
    city: CityUi?,
    mapper: AddPlaceMapper,
    actions: AddPlaceActions,
) {
    val page = remember(state.step, state.query, state.suggestions, state.name, state.address, state.groups, state.answers,
        state.hoursError, state.problem, state.saving, state.saved, city) {
        mapper.page(state, city, actions)
    }
    val draft = state.draft
    val sheet = remember(draft, state.groups) { draft?.let { mapper.hoursSheet(state, it, actions) } }
    Box(Modifier.fillMaxSize()) {
        page.Content(Modifier.fillMaxSize())
        sheet?.Content(Modifier)
    }
}
