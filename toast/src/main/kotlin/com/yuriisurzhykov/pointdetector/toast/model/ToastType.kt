package com.yuriisurzhykov.pointdetector.toast.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import com.yuriisurzhykov.pointdetector.designsystem.icons.PdIcons
import com.yuriisurzhykov.pointdetector.designsystem.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ColorRole

/**
 * Kind of a toast. Each kind hands out its own colour role and icon, plus the queue policy:
 * a higher [priority] pre-empts a lower one, a [isSticky] toast cannot be swiped away.
 */
@Immutable
sealed interface ToastType {

    val priority: Int
    val isSticky: Boolean
    val defaultDuration: ToastDuration
    val icon: PdDrawableResolver

    @get:[Composable ReadOnlyComposable]
    val statusRole: ColorRole

    data object Info : ToastType {
        override val priority: Int = 0
        override val isSticky: Boolean = false
        override val defaultDuration: ToastDuration = ToastDuration.Timed()
        override val icon: PdDrawableResolver = PdIcons.Info
        override val statusRole: ColorRole
            @[Composable ReadOnlyComposable]
            get() = PdTheme.colors.status.info
    }

    data object Success : ToastType {
        override val priority: Int = 1
        override val isSticky: Boolean = false
        override val defaultDuration: ToastDuration = ToastDuration.Timed()
        override val icon: PdDrawableResolver = PdIcons.Check
        override val statusRole: ColorRole
            @[Composable ReadOnlyComposable]
            get() = PdTheme.colors.status.success
    }

    data object Warning : ToastType {
        override val priority: Int = 2
        override val isSticky: Boolean = false
        override val defaultDuration: ToastDuration = ToastDuration.Indefinite
        override val icon: PdDrawableResolver = PdIcons.Warning
        override val statusRole: ColorRole
            @[Composable ReadOnlyComposable]
            get() = PdTheme.colors.status.warning
    }

    data object Error : ToastType {
        override val priority: Int = 3
        override val isSticky: Boolean = true
        override val defaultDuration: ToastDuration = ToastDuration.Indefinite
        override val icon: PdDrawableResolver = PdIcons.Error
        override val statusRole: ColorRole
            @[Composable ReadOnlyComposable]
            get() = PdTheme.colors.status.error
    }
}
