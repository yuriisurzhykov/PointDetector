package com.yuriisurzhykov.pointdetector.designsystem.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import com.yuriisurzhykov.pointdetector.designsystem.icons.PdIcons
import com.yuriisurzhykov.pointdetector.designsystem.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ColorRole

/**
 * Whether a place can be visited right now. The state itself hands out its colour role and
 * its icon, so no component ever maps "open" to a colour. "Open" is the brand blue, and the
 * three icons differ in shape (filled, half, empty), so the state never rests on colour alone.
 */
@Immutable
sealed interface PlaceStatus {

    @get:[Composable ReadOnlyComposable]
    val statusRole: ColorRole

    val icon: PdDrawableResolver

    data object Open : PlaceStatus {
        override val statusRole: ColorRole
            @[Composable ReadOnlyComposable]
            get() = PdTheme.colors.primary

        override val icon: PdDrawableResolver = PdIcons.StatusOpen
    }

    data object ClosingSoon : PlaceStatus {
        override val statusRole: ColorRole
            @[Composable ReadOnlyComposable]
            get() = PdTheme.colors.status.warning

        override val icon: PdDrawableResolver = PdIcons.StatusClosingSoon
    }

    data object Closed : PlaceStatus {
        override val statusRole: ColorRole
            @[Composable ReadOnlyComposable]
            get() = PdTheme.colors.status.neutral

        override val icon: PdDrawableResolver = PdIcons.StatusClosed
    }
}
