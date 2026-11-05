package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIconIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdSuccessIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdRadioMark
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdBackTopBar
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdSegmentedSteps
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.notice.PdOfflineNotice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.notice.PdWarningNotice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.snackbar.PdActionSnackbar
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.snackbar.PdMessageSnackbar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.bars.PdStepTopBar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdModalSheet
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdEmptyPlaces
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdLoadErrorPanel
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdNoFavorites
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdNoMatches
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewRow
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

private val MODAL_SCREEN_HEIGHT = 560.dp

@PdPreviews
@Composable
private fun PdTopBarsPreview() {
    PdPreviewFrame {
        PdPreviewLabel("PdBackTopBar")
        PdBackTopBar(pdText("Search places"), pdText("Back")) {}.Content(Modifier)
        PdPreviewLabel("PdStepTopBar: a top bar with steps (PdSegmentedSteps)")
        PdStepTopBar(
            topBar = PdBackTopBar(pdText("Saving the place"), pdText("Back")) {},
            steps = PdSegmentedSteps(step = 2, total = 3, label = pdText("Step 2 of 3")),
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdIllustrationsPreview() {
    PdPreviewFrame {
        PdPreviewLabel("large, small, success, and the radio mark")
        PdPreviewRow {
            PdIconIllustration(PdIcons.Locate).Content(Modifier)
            PdIconIllustration(PdIcons.Heart, large = false).Content(Modifier)
            PdSuccessIllustration(PdIcons.Check).Content(Modifier)
        }
        PdPreviewRow {
            PdRadioMark(selected = false).Content(Modifier)
            PdRadioMark(selected = true).Content(Modifier)
        }
    }
}

@PdPreviews
@Composable
private fun PdEmptyStatesPreview() {
    PdPreviewFrame {
        PdPreviewLabel("PdEmptyPlaces")
        PdEmptyPlaces(onAdd = {}, onChooseCity = {}).Content(Modifier)
        PdPreviewLabel("PdNoFavorites (no actions)")
        PdNoFavorites.Content(Modifier)
        PdPreviewLabel("PdNoMatches")
        PdNoMatches(pdText("pantri"), onAdd = {}).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdLoadErrorPreview() {
    PdPreviewFrame {
        var retries by remember { mutableIntStateOf(0) }
        PdPreviewLabel("PdOfflineNotice and PdWarningNotice")
        PdOfflineNotice.Content(Modifier)
        PdWarningNotice(PdIcons.Warning, pdText("Some places may be out of date")).Content(Modifier)
        PdPreviewLabel("PdLoadErrorPanel: retries $retries")
        PdLoadErrorPanel(
            message = pdText("The list of cities is not loaded yet. Check the connection and try again."),
            onRetry = { retries++ },
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdSnackbarsPreview() {
    PdPreviewFrame {
        var undone by remember { mutableStateOf(false) }
        PdPreviewLabel(if (undone) "undone" else "tap Undo")
        PdActionSnackbar(pdText("Removed from favorites"), pdText("Undo")) { undone = !undone }.Content(Modifier)
        PdMessageSnackbar(pdText("To grant the permission, open the settings.")).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdModalSheetPreview() {
    PdTheme {
        var open by remember { mutableStateOf(true) }
        Box(
            Modifier
                .fillMaxWidth()
                .height(MODAL_SCREEN_HEIGHT)
                .background(PdTheme.colors.map.land),
        ) {
            PdPrimaryButton(pdText("Open the sheet"), PdDecorativeIcon(PdIcons.Tune, tone = PdContentTone.Inherit)) { open = true }
                .Content(Modifier.align(Alignment.Center).padding(PdTheme.dimensions.spacing.regular))
            if (open) {
                PdModalSheet(
                    title = pdText("Choose a city"),
                    closeDescription = pdText("Close"),
                    onClose = { open = false },
                    content = PdPlainText(pdText("The content of the sheet. Tap the dimmed area or the cross to close it.")),
                    footer = PdPrimaryButton(pdText("Apply")) { open = false },
                ).Content(Modifier.fillMaxSize())
            }
        }
    }
}
