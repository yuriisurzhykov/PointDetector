package com.yuriisurzhykov.pointdetector.uicomponents.preview.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdErrorFieldMessage
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdHintFieldMessage
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdNoFieldMessage
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdSearchField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdTextField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdTimeField
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

@PdPreviews
@Composable
private fun PdTextFieldPreview() {
    PdPreviewFrame {
        var name by remember { mutableStateOf("Midtown Community Pantry") }
        var empty by remember { mutableStateOf("") }
        PdPreviewLabel("type into the fields; the frame turns blue on focus")
        PdTextField(pdText("Place name"), name, { name = it }, pdText("Enter the name")).Content(Modifier)
        PdTextField(
            label = pdText("Address"),
            value = empty,
            onValueChange = { empty = it },
            hint = pdText("Enter the address"),
            message = PdHintFieldMessage(pdText("As on the sign, with the city")),
        ).Content(Modifier)
        PdTextField(
            label = pdText("Place name"),
            value = "",
            onValueChange = {},
            hint = pdText("Enter the name"),
            message = PdErrorFieldMessage(pdText("The name cannot be empty")),
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdFieldMessagePreview() {
    PdPreviewFrame {
        PdPreviewLabel("PdNoFieldMessage draws nothing between these two lines")
        PdNoFieldMessage.Content(Modifier)
        PdHintFieldMessage(pdText("A quiet line of help")).Content(Modifier)
        PdErrorFieldMessage(pdText("Days of work are not selected")).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdSearchFieldPreview() {
    PdPreviewFrame {
        var query by remember { mutableStateOf("2210 Cap") }
        var searches by remember { mutableStateOf(0) }
        PdPreviewLabel("searches: $searches. Clear it with the cross.")
        PdSearchField(
            value = query,
            onValueChange = { query = it },
            hint = pdText("Enter the address"),
            clearDescription = pdText("Clear"),
            onSearch = { searches++ },
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdTimeFieldPreview() {
    PdPreviewFrame {
        var from by remember { mutableStateOf("0900") }
        var until by remember { mutableStateOf("0800") }
        PdPreviewLabel("only digits are kept and shown as a time")
        PdTimeField(pdText("From"), from, { from = it }).Content(Modifier)
        PdTimeField(
            label = pdText("To"),
            digits = until,
            onDigitsChange = { until = it },
            message = if (until.length == 4 && until <= from) {
                PdErrorFieldMessage(pdText("The end is not after the start"))
            } else {
                PdNoFieldMessage
            },
        ).Content(Modifier)
    }
}
