package com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields

import androidx.compose.runtime.Stable
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent

/** What stands under a field: nothing, a hint or an error. The field asks it whether it is an error. */
@Stable
interface PdFieldMessage : PdComponent {
    val isError: Boolean
}
