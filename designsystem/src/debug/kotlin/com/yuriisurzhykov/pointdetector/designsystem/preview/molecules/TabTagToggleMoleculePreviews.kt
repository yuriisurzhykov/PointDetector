package com.yuriisurzhykov.pointdetector.designsystem.preview.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.molecules.tabs.PdCountBadge
import com.yuriisurzhykov.pointdetector.designsystem.molecules.tabs.PdNoBadge
import com.yuriisurzhykov.pointdetector.designsystem.molecules.tabs.PdTextTab
import com.yuriisurzhykov.pointdetector.designsystem.molecules.tags.PdTextTag
import com.yuriisurzhykov.pointdetector.designsystem.molecules.toggles.PdHeartToggle
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewRow
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.designsystem.preview.pdText

@PdPreviews
@Composable
private fun PdCountBadgePreview() {
    PdPreviewFrame {
        PdPreviewRow {
            listOf(0, 3, 12, 128).forEach { PdCountBadge(it).Content(Modifier) }
        }
    }
}

@PdPreviews
@Composable
private fun PdNoBadgePreview() {
    PdPreviewFrame {
        PdPreviewLabel("Null Object: the tab on the left has no badge and no empty space for one")
        PdPreviewRow {
            PdTextTab(pdText("All"), PdNoBadge, selected = true, onSelect = {}).Content(Modifier)
            PdTextTab(pdText("Favorites"), PdCountBadge(2), selected = false, onSelect = {}).Content(Modifier)
        }
    }
}

@PdPreviews
@Composable
private fun PdTextTabPreview() {
    PdPreviewFrame {
        var selected by remember { mutableStateOf(true) }
        PdPreviewLabel("tap the tab: the underline fades in and out")
        PdTextTab(
            label = pdText("Near me"),
            badge = PdCountBadge(7),
            selected = selected,
            onSelect = { selected = !selected },
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdTextTagPreview() {
    PdPreviewFrame {
        PdPreviewRow {
            listOf("Groceries", "Hot meals", "Delivery").forEach { PdTextTag(pdText(it)).Content(Modifier) }
        }
    }
}

@PdPreviews
@Composable
private fun PdHeartTogglePreview() {
    PdPreviewFrame {
        var checked by remember { mutableStateOf(false) }
        PdPreviewLabel("tap the heart: a spring pop and a burst of particles when it turns on")
        PdHeartToggle(
            checked = checked,
            description = pdText("Add to favorites"),
            onCheckedChange = { checked = it },
        ).Content(Modifier)
    }
}
