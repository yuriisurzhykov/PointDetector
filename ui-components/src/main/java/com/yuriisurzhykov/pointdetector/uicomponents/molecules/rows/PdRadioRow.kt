package com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows

import androidx.compose.runtime.Immutable
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdRadioMark
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** One option of an exclusive list: a title, a subtitle and a radio mark at the end. */
@Immutable
class PdRadioRow(
    title: PdTextResolver,
    subtitle: PdComponent = PdNoComponent,
    selected: Boolean,
    onSelect: () -> Unit,
) : PdBaseListRow(title, subtitle, selected = false, onClick = onSelect) {

    override val role: Role = Role.RadioButton
    override val trailing: PdComponent = PdRadioMark(selected)
}
