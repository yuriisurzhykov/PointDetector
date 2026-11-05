package com.yuriisurzhykov.pointdetector.designsystem.molecules.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.style
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone

/**
 * One universal row: icon, caption, value. A new fact about a place (parking, languages,
 * delivery) is a new set of data for this class, never a new class.
 */
@Immutable
class PdFactItem(
    private val icon: PdDrawableResolver,
    private val label: PdTextResolver,
    private val value: PdTextResolver,
) : PdBaseComponent(), PdHelpItem {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val glyph = remember(icon) { PdDecorativeIcon(icon, PdIconSize.Regular, PdContentTone.Brand) }
        Row(
            modifier = modifier.padding(spacing.regular),
            horizontalArrangement = Arrangement.spacedBy(spacing.regular),
            verticalAlignment = Alignment.Top,
        ) {
            glyph.Content(Modifier)
            Column(verticalArrangement = Arrangement.spacedBy(spacing.xxSmall)) {
                PdBasicText(label, PdTextRole.BodySmall.style(), PdTheme.colors.content.secondary)
                PdBasicText(value, PdTextRole.LabelLarge.style(), PdTheme.colors.content.primary)
            }
        }
    }
}
