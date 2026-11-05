package com.yuriisurzhykov.pointdetector.designsystem.theme.motion

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable

/**
 * Every duration, spring and scale a component may use. Components call [spatial] / [effects]
 * and read the named numbers; they never write `tween(300)` themselves. With "reduce motion"
 * the theme swaps in [ReducedMotion], so one switch quiets the whole app.
 */
@Immutable
data class PdMotion(
    val isReduced: Boolean,
    val fastMillis: Int,
    val mediumMillis: Int,
    val slowMillis: Int,
    val staggerMillis: Int,
    val shimmerMillis: Int,
    val spinMillis: Int,
    val pulseMillis: Int,
    val springDamping: Float,
    val springStiffness: Float,
    val pressedScale: Float,
    val popPeakScale: Float,
    val flingProjectionMillis: Int,
) {
    fun <T> spatial(): SpringSpec<T> = spring(dampingRatio = springDamping, stiffness = springStiffness)

    fun <T> effects(): TweenSpec<T> = tween(durationMillis = mediumMillis)

    fun <T> quick(): TweenSpec<T> = tween(durationMillis = fastMillis)
}

val DefaultMotion = PdMotion(
    isReduced = false,
    fastMillis = 150,
    mediumMillis = 280,
    slowMillis = 450,
    staggerMillis = 55,
    shimmerMillis = 1400,
    spinMillis = 800,
    pulseMillis = 1800,
    springDamping = 0.75f,
    springStiffness = 380f,
    pressedScale = 0.96f,
    popPeakScale = 1.4f,
    flingProjectionMillis = 260,
)

val ReducedMotion = PdMotion(
    isReduced = true,
    fastMillis = 0,
    mediumMillis = 0,
    slowMillis = 0,
    staggerMillis = 0,
    shimmerMillis = 0,
    spinMillis = 0,
    pulseMillis = 0,
    springDamping = 1f,
    springStiffness = 10_000f,
    pressedScale = 1f,
    popPeakScale = 1f,
    flingProjectionMillis = 260,
)
