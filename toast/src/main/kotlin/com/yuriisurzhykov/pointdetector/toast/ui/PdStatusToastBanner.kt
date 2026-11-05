package com.yuriisurzhykov.pointdetector.toast.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.LocalPdContentColor
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.toast.model.ToastData
import com.yuriisurzhykov.pointdetector.toast.ui.animation.auroraBackground

/** A banner coloured by the toast type, with the aurora behind its message and icon. */
@Immutable
class PdStatusToastBanner(
    private val data: ToastData,
    private val contentModifier: Modifier = Modifier,
) : PdBaseComponent(), PdToastBanner {

    @Composable
    override fun Render(modifier: Modifier) {
        val role = data.type.statusRole
        val dimensions = PdTheme.dimensions
        val insets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
        val message = remember(data) {
            PdPlainText(data.message, PdTextRole.TitleMedium, PdContentTone.Inherit, TextAlign.Center)
        }
        val icon = remember(data) { PdDecorativeIcon(data.type.icon, PdIconSize.Regular, PdContentTone.Inherit) }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(PdTheme.shapes.large)
                .auroraBackground(role, animated = !PdTheme.motion.isReduced),
        ) {
            CompositionLocalProvider(LocalPdContentColor provides role.onMain) {
                Row(
                    modifier = contentModifier
                        .fillMaxWidth()
                        .windowInsetsPadding(insets)
                        .defaultMinSize(minHeight = dimensions.component.toastMinHeight)
                        .padding(horizontal = dimensions.spacing.regular, vertical = dimensions.spacing.small),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    message.Content(Modifier.weight(1f))
                    icon.Content(Modifier)
                }
            }
        }
    }
}
