package com.yuriisurzhykov.pointdetector.presentation.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdChoice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdTextButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdChoiceSegments
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdRadioRow
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.rows.PdBorderedRows
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.section.PdTitledSection
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdModalSheet
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdSheetBody
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdSheetFooter
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** What the person can do in the filters sheet. The sheet only reports; the host decides. */
interface FiltersActions {
    fun onSort(id: String)
    fun onUnit(id: String)
    fun onReset()
    fun onApply()
    fun onClose()
}

/**
 * The filters sheet: the sorting as a list of radio rows, the units as two segments and the
 * "Apply" button. When the choice has been saved, [onApplied] runs once and the host closes it.
 */
@Composable
fun FiltersSheet(state: FiltersState, actions: FiltersActions, onApplied: () -> Unit) {
    LaunchedEffect(state.applied) { if (state.applied) onApplied() }
    remember(state, actions) {
        PdModalSheet(
            title = PdStringText(R.string.title_filter_screen),
            closeDescription = PdStringText(R.string.sheet_close),
            onClose = actions::onClose,
            action = PdTextButton(PdStringText(R.string.filters_reset), actions::onReset),
            content = PdSheetBody(if (state.isReady) blocks(state, actions) else emptyList()),
            footer = PdSheetFooter(PdPrimaryButton(PdStringText(R.string.button_apply_filters), onClick = actions::onApply)),
        )
    }.Content(Modifier)
}

private fun blocks(state: FiltersState, actions: FiltersActions): List<PdComponent> = listOf(
    PdTitledSection(
        title = PdStringText(R.string.label_sorting_type),
        content = PdBorderedRows(
            state.sorts.map { option ->
                PdRadioRow(
                    title = PdLiteralText(option.label),
                    subtitle = sortHint(option.id),
                    selected = option.id == state.sortId,
                ) { actions.onSort(option.id) }
            },
        ),
    ),
    PdTitledSection(
        title = PdStringText(R.string.label_measure_type),
        content = PdChoiceSegments(
            state.units.map { option ->
                PdChoice(PdLiteralText(option.label), option.id == state.unitId) { actions.onUnit(option.id) }
            },
        ),
    ),
)

private fun sortHint(id: String) = when (id) {
    FilterDefaults.SORT -> hint(R.string.places_order_distance)
    FilterDefaults.SORT_BY_NAME -> hint(R.string.filters_sort_name_hint)
    else -> PdNoComponent
}

private fun hint(text: Int) = PdPlainText(PdStringText(text), PdTextRole.BodySmall, PdContentTone.Secondary, maxLines = 1)
