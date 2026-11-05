package com.yuriisurzhykov.pointdetector.designsystem.atoms.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

@Immutable
object PdHairlineDivider : PdBaseComponent(), PdDivider {

    @Composable
    override fun Render(modifier: Modifier) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(PdTheme.dimensions.stroke.thin)
                .background(PdTheme.colors.divider),
        )
    }
}
