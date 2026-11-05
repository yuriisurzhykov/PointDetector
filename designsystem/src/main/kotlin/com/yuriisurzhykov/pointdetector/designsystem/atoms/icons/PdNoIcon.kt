package com.yuriisurzhykov.pointdetector.designsystem.atoms.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent

/** No icon. A Null Object: draws nothing and takes no space, so no icon parameter is ever nullable. */
@Immutable
object PdNoIcon : PdBaseComponent(), PdIcon {

    @Composable
    override fun Render(modifier: Modifier) = Unit
}
