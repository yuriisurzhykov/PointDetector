package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdListItem
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdHintedStatusLine
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdSimpleStatusLine
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tags.PdTextTag
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.toggles.PdHeartToggle
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlaceCardData
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlaceCardLoading
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

private val LIST_HEIGHT = 520.dp

private fun placeCard(
    key: String,
    number: Int,
    name: String,
    status: PlaceStatus,
    statusTitle: String,
    hint: String?,
    favorite: Boolean,
    selected: Boolean,
    onFavorite: (Boolean) -> Unit,
    onOpen: () -> Unit,
): PdListItem = PdPlaceCardData(
    key = key,
    number = number,
    name = pdText(name),
    address = pdText("1200 Market Street, Sacramento"),
    distance = pdText("1.4 mi"),
    status = if (hint == null) {
        PdSimpleStatusLine(status, pdText(statusTitle))
    } else {
        PdHintedStatusLine(status, pdText(statusTitle), pdText(hint))
    },
    tags = listOf(PdTextTag(pdText("Groceries")), PdTextTag(pdText("Hot meals"))),
    favorite = PdHeartToggle(favorite, pdText("Add to favorites"), onFavorite),
    selected = selected,
    onOpen = onOpen,
)

@PdPreviews
@Composable
private fun PdPlaceCardDataPreview() {
    PdPreviewFrame {
        var favorite by remember { mutableStateOf(false) }
        var selected by remember { mutableStateOf(false) }
        PdPreviewLabel("tap the card to select it, tap the heart to favorite it")
        placeCard(
            key = "interactive",
            number = 1,
            name = "Sacramento Food Bank",
            status = PlaceStatus.Open,
            statusTitle = "Open until 6 PM",
            hint = "Next opening: tomorrow at 9 AM",
            favorite = favorite,
            selected = selected,
            onFavorite = { favorite = it },
            onOpen = { selected = !selected },
        ).Content(Modifier)
        PdPreviewLabel("a closed place")
        placeCard(
            key = "closed",
            number = 2,
            name = "Community Pantry",
            status = PlaceStatus.Closed,
            statusTitle = "Closed",
            hint = null,
            favorite = true,
            selected = false,
            onFavorite = {},
            onOpen = {},
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdPlaceCardLoadingPreview() {
    PdPreviewFrame {
        PdPlaceCardLoading("loading").Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdPlaceCardListPreview() {
    PdPreviewFrame {
        var selectedKey by remember { mutableStateOf("a") }
        val favorites = remember { mutableStateListOf<String>() }
        val cards = listOf(
            Triple("a", "Sacramento Food Bank", PlaceStatus.Open),
            Triple("b", "Community Pantry", PlaceStatus.ClosingSoon),
            Triple("c", "River Street Kitchen", PlaceStatus.Closed),
        ).mapIndexed { index, (key, name, status) ->
            placeCard(
                key = key,
                number = index + 1,
                name = name,
                status = status,
                statusTitle = "Today",
                hint = null,
                favorite = key in favorites,
                selected = key == selectedKey,
                onFavorite = { on -> if (on) favorites.add(key) else favorites.remove(key) },
                onOpen = { selectedKey = key },
            )
        } + PdPlaceCardLoading("loading-1") + PdPlaceCardLoading("loading-2")
        PdPreviewLabel("a LazyColumn driven by key and contentType of PdListItem")
        LazyColumn(
            modifier = Modifier.height(LIST_HEIGHT),
            verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.small),
        ) {
            items(cards, key = { it.key }, contentType = { it.contentType }) { it.Content(Modifier) }
        }
    }
}
