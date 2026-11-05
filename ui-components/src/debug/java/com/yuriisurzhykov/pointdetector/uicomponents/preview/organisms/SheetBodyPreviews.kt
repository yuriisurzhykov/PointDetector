package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdChoice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdTextButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdChoiceSegments
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdRadioRow
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.rows.PdBorderedRows
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.section.PdTitledSection
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdModalSheet
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdSheetBody
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdSheetFooter
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

private val SHEET_SCREEN_HEIGHT = 620.dp

@PdPreviews
@Composable
private fun PdTextButtonPreview() {
    PdPreviewFrame { PdTextButton(pdText("Reset")) {}.Content(Modifier) }
}

@PdPreviews
@Composable
private fun PdFiltersSheetPreview() {
    PdTheme {
        var sort by remember { mutableStateOf(0) }
        var miles by remember { mutableStateOf(false) }
        Box(Modifier.fillMaxWidth().height(SHEET_SCREEN_HEIGHT).background(PdTheme.colors.map.land)) {
            PdModalSheet(
                title = pdText("Filters"),
                closeDescription = pdText("Close"),
                onClose = {},
                action = PdTextButton(pdText("Reset")) { sort = 0; miles = false },
                content = PdSheetBody(
                    listOf(
                        PdTitledSection(
                            title = pdText("Sorting type"),
                            content = PdBorderedRows(
                                listOf(
                                    PdRadioRow(pdText("Nearest first"), selected = sort == 0) { sort = 0 },
                                    PdRadioRow(pdText("By name"), selected = sort == 1) { sort = 1 },
                                ),
                            ),
                        ),
                        PdTitledSection(
                            title = pdText("Measure units"),
                            content = PdChoiceSegments(
                                listOf(
                                    PdChoice(pdText("Kilometers"), !miles) { miles = false },
                                    PdChoice(pdText("Miles"), miles) { miles = true },
                                ),
                            ),
                        ),
                    ),
                ),
                footer = PdSheetFooter(PdPrimaryButton(pdText("Apply filters")) {}),
            ).Content(Modifier.fillMaxSize())
        }
    }
}

@PdPreviews
@Composable
private fun PdSheetBodyPreview() {
    PdPreviewFrame {
        PdSheetBody(listOf(PdPlainText(pdText("The first block")), PdPlainText(pdText("The second block")))).Content(Modifier)
        PdSheetFooter(PdPrimaryButton(pdText("Apply"))  {}).Content(Modifier)
    }
}
