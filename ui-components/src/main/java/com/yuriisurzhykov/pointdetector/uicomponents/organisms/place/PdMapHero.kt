package com.yuriisurzhykov.pointdetector.uicomponents.organisms.place

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMap

/** A map with one place, and the back and share buttons over its corners. */
@Immutable
class PdMapHero(
    private val map: PdMap,
    private val back: PdIconButton,
    private val share: PdIconButton,
) : PdBaseComponent(), PdHero {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        Box(modifier.fillMaxWidth().height(PdTheme.dimensions.component.placeHeroHeight)) {
            map.Content(Modifier.fillMaxSize())
            back.Content(
                Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(start = spacing.small, top = spacing.small),
            )
            share.Content(
                Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(end = spacing.small, top = spacing.small),
            )
        }
    }
}
