package com.yuriisurzhykov.pointdetector.designsystem.theme.dimens

import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.ComponentDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.ElevationDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.IconDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.StrokeDimensions

internal val FixedIconDimensions = IconDimensions(
    small = 16.dp,
    regular = 24.dp,
    large = 40.dp,
    xLarge = 64.dp,
)

internal val FixedStrokeDimensions = StrokeDimensions(
    thin = 1.dp,
    regular = 1.5.dp,
    thick = 2.dp,
)

internal val FixedElevationDimensions = ElevationDimensions(
    none = 0.dp,
    small = 2.dp,
    medium = 4.dp,
    large = 6.dp,
)

internal val FixedComponentDimensions = ComponentDimensions(
    touchTarget = 48.dp,
    buttonHeight = 52.dp,
    statusMarkSize = 14.dp,
    tagVerticalPadding = 3.dp,
    tagHorizontalPadding = 8.dp,
    tabMinHeight = 44.dp,
    badgeMinWidth = 22.dp,
    skeletonLineHeight = 14.dp,
    skeletonTitleHeight = 18.dp,
    skeletonChipHeight = 26.dp,
    skeletonWideFraction = 0.7f,
    skeletonNarrowFraction = 0.45f,
    numberMarkSize = 24.dp,
    spinnerSize = 18.dp,
    spinnerStroke = 2.5.dp,
    burstParticleSize = 5.dp,
    burstDistance = 21.dp,
    toastMinHeight = 48.dp,
    toastSwipeThreshold = 56.dp,
    timelineHeight = 60.dp,
    timelineTrackTop = 30.dp,
    timelineTrackHeight = 12.dp,
    timelineLineTop = 20.dp,
    timelineKnobSize = 12.dp,
    timelineKnobStroke = 3.dp,
)
