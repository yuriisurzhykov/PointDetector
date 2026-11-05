package com.yuriisurzhykov.pointdetector.toast.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.toast.model.ToastData
import com.yuriisurzhykov.pointdetector.toast.tokens.ToastTokens
import kotlinx.coroutines.launch

private const val OPAQUE = 1f

/**
 * Wraps the banner with an upward swipe for non-sticky types. Offset and alpha are applied
 * in a graphics layer, so drag frames redraw a layer instead of recomposing.
 */
@Composable
internal fun SwipeDismissBanner(
    data: ToastData,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    val threshold = PdTheme.dimensions.component.toastSwipeThreshold
    val thresholdPx = with(LocalDensity.current) { threshold.toPx() }

    val swipe = if (data.type.isSticky) {
        Modifier
    } else {
        Modifier.pointerInput(data.id) {
            detectVerticalDragGestures(
                onDragEnd = {
                    if (dragOffsetY < -thresholdPx) {
                        onDismiss()
                    } else {
                        scope.launch {
                            animate(
                                initialValue = dragOffsetY,
                                targetValue = 0f,
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            ) { value, _ -> dragOffsetY = value }
                        }
                    }
                },
                onDragCancel = { dragOffsetY = 0f },
                onVerticalDrag = { change, dragAmount ->
                    dragOffsetY = (dragOffsetY + dragAmount).coerceAtMost(0f)
                    change.consume()
                },
            )
        }
    }

    PdStatusToastBanner(data, swipe).Content(
        modifier.graphicsLayer {
            translationY = dragOffsetY
            val progress = (dragOffsetY / -thresholdPx).coerceIn(0f, 1f)
            alpha = OPAQUE - progress * ToastTokens.SWIPE_ALPHA_REDUCTION
        },
    )
}
