package com.yuriisurzhykov.pointdetector.uicomponents.preview.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdNumberedMapMarker
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdFlatIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlinedIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.place.PdPlaceHeader
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.search.PdSearchBar
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewRow
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.clickableMarker
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

@PdPreviews
@Composable
private fun PdIconButtonsPreview() {
    PdPreviewFrame {
        var taps by remember { mutableIntStateOf(0) }
        PdPreviewLabel("taps: $taps (flat, outlined, primary)")
        PdPreviewRow {
            PdFlatIconButton(PdIcons.Tune, pdText("Filters")) { taps++ }.Content(Modifier)
            PdOutlinedIconButton(PdIcons.Share, pdText("Share")) { taps++ }.Content(Modifier)
            PdPrimaryIconButton(PdIcons.Locate, pdText("My location")) { taps++ }.Content(Modifier)
            PdFlatIconButton(PdIcons.Back, pdText("Back")) { taps++ }.Content(Modifier)
        }
    }
}

@PdPreviews
@Composable
private fun PdSearchBarPreview() {
    PdPreviewFrame {
        var opened by remember { mutableIntStateOf(0) }
        PdPreviewLabel("tap the bar to open the search: $opened")
        PdSearchBar(
            placeholder = pdText("Find help nearby"),
            onClick = { opened++ },
            trailing = PdFlatIconButton(PdIcons.Tune, pdText("Filters")) { opened-- },
        ).Content(Modifier)
        PdPreviewLabel("without a trailing slot (PdNoComponent)")
        PdSearchBar(placeholder = pdText("Find help nearby"), onClick = { opened++ }).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdNumberedMapMarkerPreview() {
    PdPreviewFrame {
        val statuses = listOf(PlaceStatus.Open, PlaceStatus.ClosingSoon, PlaceStatus.Closed)
        var selected by remember { mutableIntStateOf(0) }
        PdPreviewLabel("open, closing soon, closed. Tap to select (grows, halo).")
        PdPreviewRow {
            statuses.forEachIndexed { index, status ->
                PdNumberedMapMarker(index + 1, status, selected == index)
                    .Content(Modifier.clickableMarker { selected = index })
            }
        }
    }
}

@PdPreviews
@Composable
private fun PdPlaceHeaderPreview() {
    PdPreviewFrame {
        PdPlaceHeader(
            name = pdText("Midtown Community Pantry"),
            address = pdText("2210 Capitol Ave, Sacramento"),
            meta = pdText("1.4 km · 18 min on foot · 4 min by car"),
        ).Content(Modifier)
    }
}
