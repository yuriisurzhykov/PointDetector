package com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** One bar per step, the passed ones filled, and a ready label such as "Step 2 of 3". */
@Immutable
class PdSegmentedSteps(
    private val step: Int,
    private val total: Int,
    private val label: PdTextResolver,
) : PdBaseComponent(), PdStepIndicator {

    init {
        require(total > 0 && step in 1..total) { "Step $step does not fit into $total steps" }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val colors = PdTheme.colors
        val description = label.resolve().toString()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.regular, vertical = spacing.small)
                .semantics(mergeDescendants = true) { contentDescription = description },
            horizontalArrangement = Arrangement.spacedBy(spacing.tiny),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(total) { index ->
                Box(
                    Modifier
                        .weight(1f)
                        .height(PdTheme.dimensions.component.stepBarHeight)
                        .background(
                            if (index < step) colors.primary.main else colors.surface.container,
                            PdTheme.shapes.full,
                        ),
                )
            }
            PdBasicText(label, PdTextRole.BodySmall.style(), colors.content.secondary, Modifier.padding(start = spacing.small), maxLines = 1)
        }
    }
}
