package com.yuriisurzhykov.pointdetector.designsystem.atoms.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** Filled, half or empty circle in the colour of the [status]. Colour and icon both come from the state. */
@Immutable
class PdPlaceStatusMark(private val status: PlaceStatus) : PdBaseComponent(), PdStatusMark {

    @Composable
    override fun Render(modifier: Modifier) {
        PdTintedImage(
            drawable = status.icon,
            tint = status.statusRole.main,
            size = PdTheme.dimensions.component.statusMarkSize,
            modifier = modifier,
        )
    }
}
