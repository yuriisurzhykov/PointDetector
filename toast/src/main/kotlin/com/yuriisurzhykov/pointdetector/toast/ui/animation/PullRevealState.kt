package com.yuriisurzhykov.pointdetector.toast.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import com.yuriisurzhykov.pointdetector.toast.tokens.ToastTokens
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val HIDDEN = 0f
private const val REVEALED = 1f

/**
 * Drives a [PullRevealShape] with staggered centre / edge timing. Entrance: the centre drops
 * first, the edges follow. Exit: the edges retract first. With `animated = false`
 * (system "reduce motion") the state jumps to its end value.
 */
@Stable
class PullRevealState internal constructor() {

    private val center = Animatable(HIDDEN)
    private val edge = Animatable(HIDDEN)

    val shape: Shape by derivedStateOf {
        PullRevealShape(centerProgress = center.value, edgeProgress = edge.value)
    }

    suspend fun enter(animated: Boolean) {
        if (!animated) return snapTo(visible = true)
        coroutineScope {
            launch { center.animateTo(REVEALED, spec(ToastTokens.REVEAL_CENTER_MS, delay = 0)) }
            launch { edge.animateTo(REVEALED, spec(ToastTokens.REVEAL_EDGE_MS, delay = ToastTokens.REVEAL_STAGGER_MS)) }
        }
    }

    suspend fun exit(animated: Boolean) {
        if (!animated) return snapTo(visible = false)
        coroutineScope {
            launch { edge.animateTo(HIDDEN, spec(ToastTokens.REVEAL_EDGE_MS, delay = 0)) }
            launch { center.animateTo(HIDDEN, spec(ToastTokens.REVEAL_CENTER_MS, delay = ToastTokens.REVEAL_STAGGER_MS)) }
        }
    }

    suspend fun snapTo(visible: Boolean) {
        val target = if (visible) REVEALED else HIDDEN
        center.snapTo(target)
        edge.snapTo(target)
    }

    private fun spec(duration: Int, delay: Int): AnimationSpec<Float> =
        tween(durationMillis = duration, delayMillis = delay, easing = FastOutSlowInEasing)
}

@Composable
fun rememberPullRevealState(): PullRevealState = remember { PullRevealState() }
