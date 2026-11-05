package com.yuriisurzhykov.pointdetector.uicomponents.preview

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdClosingSoon
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdHoursUnknown
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdOpenNow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tags.PdTextTag
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.toggles.PdHeartToggle
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlaceCard
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlaceCardData

/** Makes a preview element tappable without a ripple, only to switch a state. */
internal fun Modifier.clickableMarker(onClick: () -> Unit): Modifier = clickable(onClick = onClick)

private val PLACE_NAMES = listOf(
    "Midtown Community Pantry",
    "Loaves & Fishes Kitchen",
    "River City Food Share",
    "Oak Park Pantry",
    "Elk Grove Food Bank",
)

/** Five sample cards for the previews: the selection and the favorites are driven by the caller. */
internal fun samplePlaceCards(
    selectedKey: String,
    favorites: Set<String>,
    onSelect: (String) -> Unit,
    onFavorite: (String, Boolean) -> Unit,
): List<PdPlaceCard> = PLACE_NAMES.mapIndexed { index, name ->
    val key = "place-$index"
    PdPlaceCardData(
        key = key,
        number = index + 1,
        name = pdText(name),
        address = pdText("${2000 + index * 120} Capitol Ave, Sacramento"),
        distance = pdText("${1 + index}.${index} km"),
        status = when (index) {
            0 -> PdOpenNow(pdText("5 PM"))
            1 -> PdClosingSoon(minutesLeft = 40)
            else -> PdHoursUnknown
        },
        tags = if (index % 2 == 0) listOf(PdTextTag(pdText("Walk-in"))) else emptyList(),
        favorite = PdHeartToggle(key in favorites, pdText("Add to favorites")) { onFavorite(key, it) },
        selected = key == selectedKey,
        onOpen = { onSelect(key) },
    )
}

internal fun previewStatuses(): List<PlaceStatus> = listOf(PlaceStatus.Open, PlaceStatus.ClosingSoon, PlaceStatus.Closed)
