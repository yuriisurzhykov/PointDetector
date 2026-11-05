package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier

/** Without data: a slot that has nothing to show. It draws nothing and takes no space. */
@Immutable
object PdNoComponent : PdBaseComponent() {
    @Composable
    override fun Render(modifier: Modifier) = Unit
}
