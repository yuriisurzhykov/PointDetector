package com.yuriisurzhykov.pointdetector.presentation.city

import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdIconListRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdListRow
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone

/** The rows of the city sheet. The filter by text is plain code, apart from the screen. */
object CityRows {

    /** Cities whose name or region contains [query], ignoring case; everything when [query] is blank. */
    fun matching(cities: List<CityUi>, query: String): List<CityUi> {
        val text = query.trim()
        if (text.isEmpty()) return cities
        return cities.filter { it.name.contains(text, ignoreCase = true) || it.region.contains(text, ignoreCase = true) }
    }

    fun row(city: CityUi, selected: Boolean, onSelect: () -> Unit): PdListRow = PdIconListRow(
        icon = PdIcons.Pin,
        title = PdLiteralText(city.name),
        subtitle = if (city.region.isBlank()) PdNoComponent else PdPlainText(PdLiteralText(city.region), PdTextRole.BodySmall, PdContentTone.Secondary, maxLines = 1),
        trailing = if (selected) PdDecorativeIcon(PdIcons.Check, PdIconSize.Regular, PdContentTone.Brand) else PdNoComponent,
        selected = selected,
        onClick = onSelect,
    )
}
