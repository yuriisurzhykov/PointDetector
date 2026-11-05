package com.yuriisurzhykov.pointdetector.designsystem.theme.dimens

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.ComponentDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.ElevationDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.IconDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.LayoutDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.SpacingDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.StrokeDimensions

@Immutable
data class PdDimensions(
    val spacing: SpacingDimensions,
    val layout: LayoutDimensions,
    val icon: IconDimensions,
    val stroke: StrokeDimensions,
    val elevation: ElevationDimensions,
    val component: ComponentDimensions,
)

val CompactDimensions = PdDimensions(
    spacing = CompactSpacing,
    layout = CompactLayout,
    icon = FixedIconDimensions,
    stroke = FixedStrokeDimensions,
    elevation = FixedElevationDimensions,
    component = FixedComponentDimensions,
)

val MediumDimensions = PdDimensions(
    spacing = MediumSpacing,
    layout = MediumLayout,
    icon = FixedIconDimensions,
    stroke = FixedStrokeDimensions,
    elevation = FixedElevationDimensions,
    component = FixedComponentDimensions,
)

val ExpandedDimensions = PdDimensions(
    spacing = ExpandedSpacing,
    layout = ExpandedLayout,
    icon = FixedIconDimensions,
    stroke = FixedStrokeDimensions,
    elevation = FixedElevationDimensions,
    component = FixedComponentDimensions,
)
