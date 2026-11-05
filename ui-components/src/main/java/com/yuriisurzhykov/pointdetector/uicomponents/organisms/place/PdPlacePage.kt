package com.yuriisurzhykov.pointdetector.uicomponents.organisms.place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdRiseIn

/**
 * The hero stays on top; under it a rounded body scrolls with the [sections], which rise in one
 * after another. The [actionBar] floats over the bottom of the body.
 */
@Immutable
class PdPlacePage(
    private val hero: PdHero,
    private val sections: List<PdComponent>,
    private val actionBar: PdActionBar,
) : PdBaseComponent(), PdPage {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val component = PdTheme.dimensions.component
        Box(modifier.fillMaxSize().background(PdTheme.colors.surface.primary)) {
            Column(Modifier.fillMaxSize()) {
                hero.Content(Modifier)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .offset(y = -component.placeBodyOverlap)
                        .zIndex(1f)
                        .clip(PdTheme.shapes.sheet)
                        .background(PdTheme.colors.surface.primary)
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = spacing.regular,
                            top = spacing.large,
                            end = spacing.regular,
                            bottom = component.placeBodyBottomInset,
                        ),
                    verticalArrangement = Arrangement.spacedBy(spacing.xLarge),
                ) {
                    sections.forEachIndexed { index, section -> section.Content(Modifier.fillMaxWidth().pdRiseIn(index)) }
                }
            }
            Column(Modifier.align(Alignment.BottomCenter).zIndex(2f)) {
                actionBar.Content(Modifier)
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .background(PdTheme.colors.surface.primary)
                        .windowInsetsBottomHeight(WindowInsets.navigationBars),
                )
            }
        }
    }
}
