package com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A row without an icon: a title, an optional subtitle and a trailing mark such as a chevron. */
@Immutable
class PdPlainListRow(
    title: PdTextResolver,
    subtitle: PdComponent = PdNoComponent,
    override val trailing: PdComponent = PdNoComponent,
    selected: Boolean = false,
    onClick: () -> Unit,
) : PdBaseListRow(title, subtitle, selected, onClick)
