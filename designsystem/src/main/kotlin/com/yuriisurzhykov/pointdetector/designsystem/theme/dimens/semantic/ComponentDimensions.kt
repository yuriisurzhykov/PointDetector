package com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/** Metrics that belong to one component family. Add a field here instead of a literal in a component. */
@Immutable
data class ComponentDimensions(
    val touchTarget: Dp,
    val buttonHeight: Dp,
    val statusMarkSize: Dp,
    val tagVerticalPadding: Dp,
    val tagHorizontalPadding: Dp,
    val tabMinHeight: Dp,
    val badgeMinWidth: Dp,
    val skeletonLineHeight: Dp,
    val skeletonTitleHeight: Dp,
    val skeletonChipHeight: Dp,
    val skeletonWideFraction: Float,
    val skeletonNarrowFraction: Float,
    val numberMarkSize: Dp,
    val spinnerSize: Dp,
    val spinnerStroke: Dp,
    val burstParticleSize: Dp,
    val burstDistance: Dp,
    val toastMinHeight: Dp,
    val toastSwipeThreshold: Dp,
    val timelineHeight: Dp,
    val timelineTrackTop: Dp,
    val timelineTrackHeight: Dp,
    val timelineLineTop: Dp,
    val timelineKnobSize: Dp,
    val timelineKnobStroke: Dp,
)
