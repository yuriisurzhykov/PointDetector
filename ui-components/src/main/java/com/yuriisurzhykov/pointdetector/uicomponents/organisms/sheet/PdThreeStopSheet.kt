package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import kotlin.math.roundToInt

private const val MILLIS_IN_SECOND = 1000f

/**
 * The sheet of the main screen with three resting positions. A drag on the handle and the header
 * moves it; the content scrolls only when the sheet is fully open, otherwise a drag on the
 * content moves the sheet too. A release snaps to the nearest position, taking the speed into
 * account. The slots are components, not lambdas. [floating] rides the top edge of the sheet and
 * fades out as the sheet nears the top.
 */
@Immutable
class PdThreeStopSheet(
    private val state: PdSheetState,
    private val onStateChange: (PdSheetState) -> Unit,
    private val header: PdComponent,
    private val content: PdComponent,
    private val floating: PdComponent = PdNoComponent,
) : PdBaseComponent(), PdBottomSheet {

    @Composable
    override fun Render(modifier: Modifier) {
        BoxWithConstraints(modifier) {
            val density = LocalDensity.current
            val component = PdTheme.dimensions.component
            val motion = PdTheme.motion
            val screenHeight = with(density) { maxHeight.toPx() }
            val stops = remember(screenHeight, component) { PdSheetStops(screenHeight, component) }
            val visible = remember { mutableFloatStateOf(stops.visible(state)) }
            val scope = rememberCoroutineScope()
            val latestOnStateChange by rememberUpdatedState(onStateChange)
            val controller = remember(stops, motion) {
                PdSheetController(
                    scope = scope,
                    visible = visible,
                    stops = stops,
                    spec = motion.spatial(),
                    projectionSeconds = motion.flingProjectionMillis / MILLIS_IN_SECOND,
                    onStateChange = { latestOnStateChange(it) },
                )
            }
            LaunchedEffect(stops, state) { controller.moveTo(state) }

            val connection = remember(controller) { PdSheetNestedScroll(controller) }
            val dragState = rememberDraggableState { controller.drag(it) }
            val floatingShown by remember(stops) { derivedStateOf { stops.floatingAlpha(visible.floatValue) > 0f } }
            val expand = PdStringText(R.string.pd_sheet_expand).resolve().toString()
            val collapse = PdStringText(R.string.pd_sheet_collapse).resolve().toString()
            val colors = PdTheme.colors
            val shape = PdTheme.shapes.sheet

            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(with(density) { stops.full.toDp() })
                    .offset { IntOffset(0, (stops.full - visible.floatValue).roundToInt()) },
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .shadow(PdTheme.dimensions.elevation.large, shape)
                        .clip(shape)
                        .background(colors.surface.primary)
                        .border(PdTheme.dimensions.stroke.thin, colors.divider, shape)
                        .semantics {
                            customActions = listOf(
                                CustomAccessibilityAction(expand) {
                                    latestOnStateChange(PdSheetState.Full)
                                    true
                                },
                                CustomAccessibilityAction(collapse) {
                                    latestOnStateChange(PdSheetState.Peek)
                                    true
                                },
                            )
                        },
                ) {
                    Column(
                        Modifier.draggable(
                            state = dragState,
                            orientation = Orientation.Vertical,
                            onDragStopped = { controller.settle(it) },
                        ),
                    ) {
                        Handle(onClick = { latestOnStateChange(state.next()) })
                        header.Content(Modifier)
                    }
                    content.Content(Modifier.weight(1f).fillMaxWidth().nestedScroll(connection))
                }
                if (floatingShown) {
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = PdTheme.dimensions.spacing.small)
                            .rideAbove(with(density) { PdTheme.dimensions.spacing.small.roundToPx() })
                            .graphicsLayer { alpha = stops.floatingAlpha(visible.floatValue) },
                    ) {
                        floating.Content(Modifier)
                    }
                }
            }
        }
    }

    @Composable
    private fun Handle(onClick: () -> Unit) {
        val component = PdTheme.dimensions.component
        val description = PdStringText(R.string.pd_sheet_handle).resolve().toString()
        Box(
            Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button, onClickLabel = description, onClick = onClick)
                .padding(vertical = PdTheme.dimensions.spacing.small),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier
                    .size(component.sheetHandleWidth, component.sheetHandleHeight)
                    .background(PdTheme.colors.divider, PdTheme.shapes.full),
            )
        }
    }

    private fun PdSheetState.next(): PdSheetState = when (this) {
        PdSheetState.Peek -> PdSheetState.Half
        PdSheetState.Half -> PdSheetState.Full
        PdSheetState.Full -> PdSheetState.Peek
    }
}

/** Puts a child just above the top edge of its parent, so it rides along with a moving sheet. */
private fun Modifier.rideAbove(gap: Int): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
    layout(placeable.width, placeable.height) { placeable.place(0, -placeable.height - gap) }
}
