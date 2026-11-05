package com.yuriisurzhykov.pointdetector.uicomponents.molecules.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdChoice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdChoiceSegments
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * A question about a place with the answers to choose from, e.g. "Appointment: I do not know /
 * Required / Not required". The icon and the texts come with the kind of fact, so a new kind is
 * data, not a new class.
 */
@Immutable
class PdFactQuestion(
    private val icon: PdDrawableResolver,
    private val label: PdTextResolver,
    private val answers: List<PdChoice>,
) : PdBaseComponent(), PdHelpItem {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val glyph = remember(icon) { PdDecorativeIcon(icon, PdIconSize.Regular, PdContentTone.Brand) }
        val choices = remember(answers) { PdChoiceSegments(answers) }
        Column(modifier, verticalArrangement = Arrangement.spacedBy(spacing.small)) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.small), verticalAlignment = Alignment.CenterVertically) {
                glyph.Content(Modifier)
                PdBasicText(label, PdTextRole.TitleSmall.style(), PdTheme.colors.content.primary)
            }
            choices.Content(Modifier)
        }
    }
}
