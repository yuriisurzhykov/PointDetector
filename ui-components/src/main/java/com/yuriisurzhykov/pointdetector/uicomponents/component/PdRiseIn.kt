package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import kotlinx.coroutines.delay

private const val FULL = 1f

/**
 * The block fades in and rises a few dp, and the blocks of one screen follow each other: the
 * block with the bigger [index] starts later. The delays and the curve come from the theme, so
 * "reduce motion" shows everything at once.
 */
@Composable
internal fun Modifier.pdRiseIn(index: Int): Modifier {
    val motion = PdTheme.motion
    val offset = with(LocalDensity.current) { PdTheme.dimensions.component.riseOffset.toPx() }
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay((motion.fastMillis + index * motion.staggerMillis).toLong())
        progress.animateTo(FULL, motion.effects())
    }
    return graphicsLayer {
        alpha = progress.value
        translationY = (FULL - progress.value) * offset
    }
}
