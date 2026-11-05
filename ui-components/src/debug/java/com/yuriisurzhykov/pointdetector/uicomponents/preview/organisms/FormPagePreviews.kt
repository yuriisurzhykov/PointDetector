package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdSuccessIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdChoice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdBackTopBar
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdSegmentedSteps
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdTextField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdTimeField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdFactQuestion
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.layout.PdPairRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.search.PdSearchPrompt
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.summary.PdSummaryCard
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.bars.PdStepTopBar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.form.PdCenteredFormPage
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.form.PdFormPage
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.message.PdResultPanel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

private val FORM_PAGE_HEIGHT = 640.dp

private fun stepBar(step: Int, title: String) = PdStepTopBar(
    topBar = PdBackTopBar(pdText(title), pdText("Back")) {},
    steps = PdSegmentedSteps(step, 3, pdText("Step $step of 3")),
)

@PdPreviews
@Composable
private fun PdFormPagePreview() {
    PdTheme {
        var name by remember { mutableStateOf("Midtown Community Pantry") }
        var answer by remember { mutableIntStateOf(0) }
        val answers = listOf("I do not know", "Required", "Not required")
        PdFormPage(
            topBar = stepBar(2, "Saving the place"),
            body = listOf(
                PdTextField(pdText("Place name"), name, { name = it }, pdText("Type the name")),
                PdPairRow(
                    PdTimeField(pdText("From"), "0900", {}),
                    PdTimeField(pdText("To"), "1700", {}),
                ),
                PdFactQuestion(
                    icon = PdIcons.Calendar,
                    label = pdText("Appointment"),
                    answers = answers.mapIndexed { index, text -> PdChoice(pdText(text), answer == index) { answer = index } },
                ),
            ),
            footer = listOf(PdPrimaryButton(pdText("Save")) {}),
        ).Content(Modifier.fillMaxWidth().height(FORM_PAGE_HEIGHT))
    }
}

@PdPreviews
@Composable
private fun PdCenteredFormPagePreview() {
    PdTheme {
        PdCenteredFormPage(
            topBar = stepBar(3, "Saving the place"),
            body = listOf(
                PdResultPanel(
                    illustration = PdSuccessIllustration(PdIcons.Check),
                    title = pdText("The place has been added!"),
                    summary = PdSummaryCard(
                        pdText("Midtown Community Pantry"),
                        listOf(pdText("2210 Capitol Ave, Sacramento"), pdText("Mon–Fri · 09:00 – 17:00")),
                    ),
                ),
            ),
            footer = listOf(
                PdPrimaryButton(pdText("Show on the map")) {},
                PdOutlineButton(pdText("Add another one")) {},
            ),
        ).Content(Modifier.fillMaxWidth().height(FORM_PAGE_HEIGHT))
    }
}

@PdPreviews
@Composable
private fun PdSearchPromptPreview() {
    PdPreviewFrame { PdSearchPrompt(pdText("Start typing above and the possible places will appear here")).Content(Modifier) }
}
