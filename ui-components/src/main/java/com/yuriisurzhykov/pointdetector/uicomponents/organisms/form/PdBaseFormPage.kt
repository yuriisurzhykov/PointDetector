package com.yuriisurzhykov.pointdetector.uicomponents.organisms.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdTopBar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPage

/**
 * The shared skeleton of a screen with a form: the top bar, a scrolling body and the actions
 * pinned at the bottom under a hairline. A subclass decides where the body sits when it is
 * shorter than the screen: at the top (a form) or in the middle (a result).
 */
@Immutable
abstract class PdBaseFormPage(
    private val topBar: PdTopBar,
    private val body: List<PdComponent>,
    private val footer: List<PdComponent>,
) : PdBaseComponent(), PdPage {

    @Composable
    protected abstract fun placement(): Arrangement.Vertical

    @Composable
    final override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        Column(modifier.fillMaxSize().background(PdTheme.colors.surface.primary)) {
            topBar.Content(Modifier)
            BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = maxHeight)
                        .verticalScroll(rememberScrollState())
                        .padding(spacing.regular),
                    verticalArrangement = placement(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    body.forEach { it.Content(Modifier.fillMaxWidth()) }
                }
            }
            if (footer.isNotEmpty()) {
                PdHairlineDivider.Content(Modifier)
                Column(
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(spacing.regular),
                    verticalArrangement = Arrangement.spacedBy(spacing.small),
                ) {
                    footer.forEach { it.Content(Modifier.fillMaxWidth()) }
                }
            }
        }
    }
}
