package com.yuriisurzhykov.pointdetector.designsystem.theme.dimens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.LayoutDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.SpacingDimensions

internal val CompactSpacing = SpacingDimensions(
    xxSmall = 2.dp,
    tiny = 4.dp,
    small = 8.dp,
    regular = 16.dp,
    large = 20.dp,
    xLarge = 24.dp,
    xxLarge = 32.dp,
)

internal val MediumSpacing = SpacingDimensions(
    xxSmall = 2.dp,
    tiny = 6.dp,
    small = 10.dp,
    regular = 20.dp,
    large = 24.dp,
    xLarge = 32.dp,
    xxLarge = 40.dp,
)

internal val ExpandedSpacing = SpacingDimensions(
    xxSmall = 4.dp,
    tiny = 8.dp,
    small = 12.dp,
    regular = 24.dp,
    large = 28.dp,
    xLarge = 40.dp,
    xxLarge = 48.dp,
)

internal val CompactLayout = LayoutDimensions(
    contentMaxWidth = Dp.Infinity,
    contentPadding = 16.dp,
    gridColumns = 1,
)

internal val MediumLayout = LayoutDimensions(
    contentMaxWidth = 600.dp,
    contentPadding = 24.dp,
    gridColumns = 1,
)

internal val ExpandedLayout = LayoutDimensions(
    contentMaxWidth = 840.dp,
    contentPadding = 32.dp,
    gridColumns = 2,
)
