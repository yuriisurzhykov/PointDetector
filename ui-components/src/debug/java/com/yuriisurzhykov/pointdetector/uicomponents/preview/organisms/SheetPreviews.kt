package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdNumberedMapMarker
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdFlatIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.search.PdSearchBar
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tabs.PdCountBadge
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tabs.PdTextTab
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.toggles.PdHeartToggle
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdCardList
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlacesHeader
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPlaceActionBar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdThreeStopSheet
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.tabs.PdTextTabs
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText
import com.yuriisurzhykov.pointdetector.uicomponents.preview.previewStatuses
import com.yuriisurzhykov.pointdetector.uicomponents.preview.samplePlaceCards

private val SCREEN_HEIGHT = 690.dp
private val PIN_SPOTS = listOf(60.dp to 150.dp, 190.dp to 90.dp, 250.dp to 210.dp)

@PdPreviews
@Composable
private fun PdThreeStopSheetPreview() {
    PdTheme {
        var state by remember { mutableStateOf(PdSheetState.Peek) }
        var selected by remember { mutableStateOf("place-0") }
        var tab by remember { mutableIntStateOf(0) }
        var favorites by remember { mutableStateOf(setOf<String>()) }
        val cards = samplePlaceCards(
            selectedKey = selected,
            favorites = favorites,
            onSelect = { selected = it },
            onFavorite = { key, on -> favorites = if (on) favorites + key else favorites - key },
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(SCREEN_HEIGHT)
                .background(PdTheme.colors.map.land),
        ) {
            previewStatuses().forEachIndexed { index, status ->
                val (x, y) = PIN_SPOTS[index]
                PdNumberedMapMarker(index + 1, status, selected == "place-$index")
                    .Content(Modifier.offset(x, y))
            }
            PdSearchBar(
                placeholder = pdText("Find help nearby"),
                onClick = {},
                trailing = PdFlatIconButton(PdIcons.Tune, pdText("Filters")) {},
            ).Content(Modifier.align(Alignment.TopCenter).padding(PdTheme.dimensions.spacing.regular))
            PdThreeStopSheet(
                state = state,
                onStateChange = { state = it },
                header = PdPlacesHeader(
                    title = pdText("${cards.size} places nearby"),
                    order = pdText("Nearest first"),
                    tabs = PdTextTabs(
                        listOf("All" to 5, "Open now" to 3, "Favorites" to favorites.size).mapIndexed { index, (label, count) ->
                            PdTextTab(pdText(label), PdCountBadge(count), tab == index) { tab = index }
                        },
                    ),
                ),
                content = PdCardList(cards),
                floating = PdPrimaryIconButton(PdIcons.Locate, pdText("My location")) {},
            ).Content(Modifier.fillMaxSize())
        }
    }
}

@PdPreviews
@Composable
private fun PdCardListPreview() {
    PdPreviewFrame {
        var selected by remember { mutableStateOf("place-1") }
        PdPreviewLabel("a lazy column keyed by the cards")
        PdCardList(
            samplePlaceCards(selected, emptySet(), { selected = it }, { _, _ -> }),
        ).Content(Modifier.height(SCREEN_HEIGHT / 2))
    }
}

@PdPreviews
@Composable
private fun PdPlacesHeaderPreview() {
    PdPreviewFrame {
        var tab by remember { mutableIntStateOf(0) }
        PdPlacesHeader(
            title = pdText("5 places nearby"),
            order = pdText("Nearest first"),
            tabs = PdTextTabs(
                listOf("All" to 5, "Open now" to 3, "Favorites" to 1).mapIndexed { index, (label, count) ->
                    PdTextTab(pdText(label), PdCountBadge(count), tab == index) { tab = index }
                },
            ),
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdPlaceActionBarPreview() {
    PdPreviewFrame {
        var favorite by remember { mutableStateOf(false) }
        PdPlaceActionBar(
            primary = PdPrimaryButton(pdText("Get directions"), PdDecorativeIcon(PdIcons.Route)) {},
            favorite = PdHeartToggle(favorite, pdText("Add to favorites")) { favorite = it },
        ).Content(Modifier)
    }
}
