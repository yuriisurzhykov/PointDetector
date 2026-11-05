package com.yuriisurzhykov.pointdetector.presentation.places

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdNumberedMapMarker
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdListScroll
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdNoScroll
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdScrollTo
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdFocusOn
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdGeoPoint
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapFocus
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapPlace
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapScene
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapUser
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdNoFocus
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdNoUser
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdUserAt
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdFlatIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlinedIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdCityChip
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdSearchField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.search.PdSearchBar
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tabs.PdCountBadge
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tabs.PdTextTab
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdCardList
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlaceCardLoading
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlacesHeader
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMapFactory
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdThreeStopSheet
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdEmptyPlaces
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdNoFavorites
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdNoMatches
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdNoOpenNow
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.tabs.PdTextTabs
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdPluralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import kotlinx.coroutines.delay

private const val LOADING_CARDS = 3

/**
 * The main screen of the redesign: the map, the search and the sheet with the cards. The state is
 * turned into components here, with `remember`, so the ViewModel stays free of them. Which map is
 * drawn is decided by [mapFactory]; the screen only knows the contract.
 */
@Composable
fun PlacesScreen(
    state: PlacesUiState,
    actions: PlacesActions,
    mapFactory: PdMapFactory,
    cards: PlaceCardMapper,
    statuses: PlaceStatusMapper,
    clock: PlacesClock = rememberPlacesClock(),
) {
    val model = remember(state.places, state.tab, clock) { PlacesModel.build(state.places, state.tab, clock) }
    val component = PdTheme.dimensions.component
    val spacing = PdTheme.dimensions.spacing

    BackHandler(enabled = state.searching) { actions.onSearching(false) }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val coveredFraction = if (state.sheet == PdSheetState.Peek) component.sheetPeekFraction else component.sheetHalfFraction
        val covered = maxHeight * coveredFraction

        val mapPlaces = remember(model.rows, state.selectedId) {
            model.rows.mapIndexedNotNull { index, row ->
                val position = geoPoint(row.point.coordinates.lat, row.point.coordinates.lng) ?: return@mapIndexedNotNull null
                val selected = row.point.id == state.selectedId
                PdMapPlace(
                    id = row.point.id,
                    position = position,
                    marker = PdNumberedMapMarker(index + 1, statuses.status(row.standing), selected),
                    raised = selected,
                )
            }
        }
        val user: PdMapUser = remember(state.user) {
            state.user?.let { spot -> geoPoint(spot.latitude, spot.longitude)?.let(::PdUserAt) } ?: PdNoUser
        }
        val focus: PdMapFocus = remember(state.camera) {
            state.camera?.let { request ->
                geoPoint(request.latitude, request.longitude)?.let { PdFocusOn(it, request.token) }
            } ?: PdNoFocus
        }
        val scene = remember(mapPlaces, user, focus, covered) {
            PdMapScene(mapPlaces, user, focus, covered) { actions.onSelect(it) }
        }
        val map = remember(scene) { mapFactory.create(scene) }
        map.Content(Modifier.fillMaxSize())

        AnimatedVisibility(
            visible = state.sheet != PdSheetState.Full,
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(spacing.regular),
                verticalArrangement = Arrangement.spacedBy(spacing.small),
            ) {
                searchEntry(state, actions).Content(Modifier)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    PdCityChip(
                        label = state.city?.let { PdLiteralText(it.label) } ?: PdStringText(R.string.city_not_selected),
                        description = PdStringText(R.string.places_city_description),
                        onClick = actions::onCity,
                    ).Content(Modifier)
                    PdOutlinedIconButton(PdIcons.Plus, PdStringText(R.string.places_add), actions::onAdd).Content(Modifier)
                }
            }
        }

        val header = remember(model, state.tab, state.sortedByName) { header(model, state, actions) }
        val content = remember(model, state, cards) { content(model, state, actions, cards) }
        val locate = remember(actions) { PdPrimaryIconButton(PdIcons.Locate, PdStringText(R.string.places_my_location), actions::onLocate) }
        PdThreeStopSheet(
            state = state.sheet,
            onStateChange = actions::onSheet,
            header = header,
            content = content,
            floating = locate,
        ).Content(Modifier.fillMaxSize())
    }
}

private fun searchEntry(state: PlacesUiState, actions: PlacesActions): PdComponent =
    if (state.searching) {
        PdSearchField(
            value = state.query,
            onValueChange = actions::onQuery,
            hint = PdStringText(R.string.places_search_hint),
            clearDescription = PdStringText(R.string.places_clear),
            onSearch = {},
            autoFocus = true,
        )
    } else {
        PdSearchBar(
            placeholder = PdStringText(R.string.places_search_hint),
            onClick = { actions.onSearching(true) },
            trailing = PdFlatIconButton(PdIcons.Tune, PdStringText(R.string.places_filters), actions::onFilters),
        )
    }

private fun header(model: PlacesModel, state: PlacesUiState, actions: PlacesActions): PdComponent {
    val tabs = PlacesTab.entries.map { tab ->
        PdTextTab(
            label = PdStringText(tabLabel(tab)),
            badge = PdCountBadge(model.counts.getValue(tab)),
            selected = state.tab == tab,
            onSelect = { actions.onTab(tab) },
        )
    }
    return PdPlacesHeader(
        title = title(state.tab, model.counts.getValue(state.tab)),
        order = PdStringText(if (state.sortedByName) R.string.places_order_name else R.string.places_order_distance),
        tabs = PdTextTabs(tabs),
    )
}

private fun content(model: PlacesModel, state: PlacesUiState, actions: PlacesActions, cards: PlaceCardMapper): PdComponent {
    if (state.isLoading) return PdCardList(List(LOADING_CARDS) { PdPlaceCardLoading("loading-$it") })
    if (model.rows.isNotEmpty()) {
        val list = model.rows.mapIndexed { index, row ->
            cards.card(
                row = row,
                number = index + 1,
                selected = row.point.id == state.selectedId,
                onOpen = { point ->
                    actions.onSelect(point.id)
                    actions.onOpenPlace(point)
                },
                onFavorite = actions::onFavorite,
            )
        }
        return PdCardList(list, scrollOf(state))
    }
    return when {
        state.query.isNotBlank() -> PdNoMatches(PdLiteralText(state.query), actions::onAdd)
        state.tab == PlacesTab.Favorites -> PdNoFavorites
        state.tab == PlacesTab.OpenNow && state.places.isNotEmpty() -> PdNoOpenNow
        else -> PdEmptyPlaces(onAdd = actions::onAdd, onChooseCity = actions::onCity)
    }
}

private fun scrollOf(state: PlacesUiState): PdListScroll =
    state.scroll?.let { PdScrollTo("place-${it.pointId}", it.token) } ?: PdNoScroll

private fun title(tab: PlacesTab, count: Int): PdTextResolver = when (tab) {
    PlacesTab.All -> PdPluralText(R.plurals.places_title_all, count)
    PlacesTab.OpenNow -> PdStringText(R.string.places_title_open, listOf(PdLiteralText(count.toString())))
    PlacesTab.Favorites -> PdStringText(R.string.places_title_favorites, listOf(PdLiteralText(count.toString())))
}

private fun tabLabel(tab: PlacesTab): Int = when (tab) {
    PlacesTab.All -> R.string.places_tab_all
    PlacesTab.OpenNow -> R.string.places_tab_open
    PlacesTab.Favorites -> R.string.places_tab_favorites
}

/** A point of the data, or nothing when the data holds coordinates that are not on the Earth. */
private fun geoPoint(latitude: Double, longitude: Double): PdGeoPoint? =
    runCatching { PdGeoPoint(latitude, longitude) }.getOrNull()

@Composable
internal fun rememberPlacesClock(): PlacesClock {
    var clock by remember { mutableStateOf(PlacesClock.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(PlacesClock.millisToNextMinute())
            clock = PlacesClock.now()
        }
    }
    return clock
}
