package com.yuriisurzhykov.pointdetector.presentation.city

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdSearchField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.notice.PdWarningNotice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdIconListRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdSkeletonListRow
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.rows.PdBorderedRows
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdModalSheet
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdSheetBody
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdLoadErrorPanel
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

private const val SKELETON_ROWS = 5

/** What the person can do in the city sheet. The sheet only reports; the host decides. */
interface CityPickerActions {
    fun onSelect(city: CityUi)
    fun onDetect()
    fun onRetry()
    fun onClose()
}

/**
 * The city sheet: a search field, the way to take the city by location and the list. The state
 * of the list decides what is under the field: skeletons, the error with a retry, or the cities.
 */
@Composable
fun CityPickerSheet(
    list: CityListState,
    selectedCode: String?,
    detectionFailed: Boolean,
    actions: CityPickerActions,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val body = remember(list, selectedCode, detectionFailed, query, actions) {
        body(list, selectedCode, detectionFailed, query, actions) { query = it }
    }
    remember(body, actions) {
        PdModalSheet(
            title = PdStringText(R.string.title_dialog_choose_city),
            closeDescription = PdStringText(R.string.sheet_close),
            onClose = actions::onClose,
            content = PdSheetBody(body),
        )
    }.Content(Modifier)
}

private fun body(
    list: CityListState,
    selectedCode: String?,
    detectionFailed: Boolean,
    query: String,
    actions: CityPickerActions,
    onQuery: (String) -> Unit,
): List<PdComponent> {
    val search = PdSearchField(
        value = query,
        onValueChange = onQuery,
        hint = PdStringText(R.string.city_search_hint),
        clearDescription = PdStringText(R.string.places_clear),
        onSearch = {},
    )
    return when (list) {
        is CityListState.Loading -> listOf(search, PdBorderedRows(List(SKELETON_ROWS) { PdSkeletonListRow }))
        is CityListState.Failed -> listOf(search, PdLoadErrorPanel(PdStringText(R.string.message_cities_not_loaded), actions::onRetry))
        is CityListState.Loaded -> loaded(search, list.cities, selectedCode, detectionFailed, query, actions)
    }
}

private fun loaded(
    search: PdComponent,
    cities: List<CityUi>,
    selectedCode: String?,
    detectionFailed: Boolean,
    query: String,
    actions: CityPickerActions,
): List<PdComponent> {
    val auto = PdIconListRow(
        icon = PdIcons.Locate,
        title = PdStringText(R.string.city_auto),
        subtitle = PdPlainText(PdStringText(R.string.city_auto_hint), PdTextRole.BodySmall, PdContentTone.Secondary, maxLines = 1),
        trailing = PdDecorativeIcon(PdIcons.Chevron, PdIconSize.Regular, PdContentTone.Secondary),
        onClick = actions::onDetect,
    )
    val shown = CityRows.matching(cities, query)
    val result = mutableListOf(search, PdBorderedRows(listOf(auto)))
    if (detectionFailed) result += PdWarningNotice(PdIcons.Warning, PdStringText(R.string.city_auto_failed))
    result += if (shown.isEmpty()) {
        PdPlainText(PdStringText(R.string.city_no_matches), PdTextRole.BodyMedium, PdContentTone.Secondary)
    } else {
        PdBorderedRows(shown.map { city -> CityRows.row(city, city.code == selectedCode) { actions.onSelect(city) } })
    }
    return result
}
