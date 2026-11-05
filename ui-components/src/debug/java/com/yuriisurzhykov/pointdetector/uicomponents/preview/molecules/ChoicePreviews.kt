package com.yuriisurzhykov.pointdetector.uicomponents.preview.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdChoice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdChipRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdChoiceSegments
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdDayChip
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdIconListRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdPlainListRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdRadioRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdSkeletonListRow
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.rows.PdBorderedRows
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

private val CITIES = listOf("Sacramento" to "42 places", "Elk Grove" to "11 places", "Folsom" to "8 places")
private val DAYS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

private fun caption(text: String) = PdPlainText(pdText(text), PdTextRole.BodySmall, PdContentTone.Secondary)

@PdPreviews
@Composable
private fun PdListRowsPreview() {
    PdPreviewFrame {
        var city by remember { mutableIntStateOf(0) }
        PdPreviewLabel("PdIconListRow: tap a city, the check moves")
        PdBorderedRows(
            CITIES.mapIndexed { index, (name, count) ->
                PdIconListRow(
                    icon = PdIcons.Pin,
                    title = pdText(name),
                    subtitle = caption(count),
                    trailing = if (city == index) PdDecorativeIcon(PdIcons.Check, PdIconSize.Regular, PdContentTone.Brand) else PdNoComponent,
                    selected = city == index,
                    onClick = { city = index },
                )
            },
        ).Content(Modifier)
        PdPreviewLabel("PdPlainListRow with a chevron, and a skeleton row")
        PdBorderedRows(
            listOf(
                PdPlainListRow(
                    title = pdText("Select working hours"),
                    subtitle = caption("Nothing is set"),
                    trailing = PdDecorativeIcon(PdIcons.Chevron, PdIconSize.Regular, PdContentTone.Secondary),
                    onClick = {},
                ),
                PdSkeletonListRow,
            ),
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdRadioRowPreview() {
    PdPreviewFrame {
        var sort by remember { mutableIntStateOf(0) }
        PdPreviewLabel("an exclusive choice")
        PdBorderedRows(
            listOf("By distance" to "Nearest first", "By name" to "From A to Z").mapIndexed { index, (title, hint) ->
                PdRadioRow(pdText(title), caption(hint), selected = sort == index) { sort = index }
            },
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdChoiceSegmentsPreview() {
    PdPreviewFrame {
        var unit by remember { mutableIntStateOf(0) }
        var fact by remember { mutableIntStateOf(0) }
        PdPreviewLabel("two options")
        PdChoiceSegments(
            listOf("Kilometers", "Miles").mapIndexed { index, name -> PdChoice(pdText(name), unit == index) { unit = index } },
        ).Content(Modifier)
        PdPreviewLabel("three options: unknown stays the default")
        PdChoiceSegments(
            listOf("Not known", "Required", "Not required").mapIndexed { index, name ->
                PdChoice(pdText(name), fact == index) { fact = index }
            },
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdDayChipPreview() {
    PdPreviewFrame {
        var days by remember { mutableStateOf(setOf(0, 1, 2, 3, 4)) }
        PdPreviewLabel("tap the days: ${days.size} selected")
        PdChipRow(
            DAYS.mapIndexed { index, name ->
                PdDayChip(pdText(name), index in days) { on -> days = if (on) days + index else days - index }
            },
        ).Content(Modifier)
    }
}
