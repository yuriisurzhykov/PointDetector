package com.yuriisurzhykov.pointdetector.toast.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.toast.ToastHostState
import com.yuriisurzhykov.pointdetector.toast.ToastSlot
import com.yuriisurzhykov.pointdetector.toast.ui.animation.rememberPullRevealState

/**
 * Renders the toast of [hostState] with a pull-reveal entrance and, for non-sticky types,
 * swipe-to-dismiss. Both animations collapse to an instant change under "reduce motion".
 */
@Immutable
class PdSwipeToastHost(private val hostState: ToastHostState) : PdBaseComponent(), PdToastHost {

    @Composable
    override fun Render(modifier: Modifier) {
        val slot = hostState.slot
        val reveal = rememberPullRevealState()
        val animated = !PdTheme.motion.isReduced
        var displayed by remember { mutableStateOf<ToastSlot>(ToastSlot.Empty) }

        LaunchedEffect(slot) {
            if (slot == displayed) return@LaunchedEffect
            if (displayed is ToastSlot.Showing) reveal.exit(animated)
            displayed = slot
            if (slot is ToastSlot.Showing) {
                withFrameNanos { }
                reveal.enter(animated)
            }
        }

        when (val shown = displayed) {
            is ToastSlot.Empty -> Unit
            is ToastSlot.Showing -> Box(
                modifier = modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        clip = true
                        shape = reveal.shape
                    },
            ) {
                key(shown.data.id) {
                    SwipeDismissBanner(
                        data = shown.data,
                        onDismiss = { hostState.dismiss(shown.data.id) },
                        modifier = Modifier.padding(
                            horizontal = PdTheme.dimensions.spacing.regular,
                            vertical = PdTheme.dimensions.spacing.small,
                        ),
                    )
                }
            }
        }
    }
}
