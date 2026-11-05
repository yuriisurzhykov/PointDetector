package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdFactItem
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tabs.PdCountBadge
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tabs.PdNoBadge
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tabs.PdTextTab
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdHelpList
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdNoHelpSection
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.tabs.PdTextTabs
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

@PdPreviews
@Composable
private fun PdHelpListPreview() {
    PdPreviewFrame {
        PdHelpList(
            title = pdText("How to get help"),
            items = listOf(
                PdFactItem(PdIcons.Calendar, pdText("Appointment"), pdText("Required: call a day ahead")),
                PdFactItem(PdIcons.Id, pdText("Documents"), pdText("Not required")),
            ),
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdNoHelpSectionPreview() {
    PdPreviewFrame {
        PdPreviewLabel("Null Object: nothing between the two texts, because the place has no help facts")
        PdPlainText(pdText("Place name")).Content(Modifier)
        PdNoHelpSection.Content(Modifier)
        PdPlainText(pdText("Next section")).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdTextTabsPreview() {
    PdPreviewFrame {
        var selected by remember { mutableIntStateOf(0) }
        val tabs = listOf(
            Triple("All", PdCountBadge(24), 0),
            Triple("Open now", PdCountBadge(9), 1),
            Triple("Favorites", PdCountBadge(3), 2),
            Triple("Hidden", PdCountBadge(0), 3),
        )
        PdPreviewLabel("tap a tab; the row scrolls sideways when the tabs do not fit")
        PdTextTabs(
            tabs = tabs.map { (label, badge, index) ->
                PdTextTab(pdText(label), badge, selected == index, onSelect = { selected = index })
            } + PdTextTab(pdText("No badge"), PdNoBadge, selected == tabs.size, onSelect = { selected = tabs.size }),
        ).Content(Modifier)
    }
}
