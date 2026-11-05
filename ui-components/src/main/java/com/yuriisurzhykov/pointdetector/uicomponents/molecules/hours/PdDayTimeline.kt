package com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdAxisPlacement
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdAxisMark
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdScrub
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdScrubOff
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdWorkingDay
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/**
 * The working day on an axis with the current moment on it. Work segments use the primary
 * colour, the tail from `warnFrom` uses the "closing soon" colour. The axis is dragged only when
 * [scrub] is enabled; a read-only timeline is described to TalkBack by its summary.
 */
@Immutable
class PdDayTimeline(
    private val day: PdWorkingDay,
    private val now: PdAxisMark,
    private val ticks: List<PdAxisMark>,
    private val scrub: PdScrub = PdScrubOff,
) : PdBaseComponent(), PdHoursTimeline {

    @Composable
    override fun Render(modifier: Modifier) {
        val component = PdTheme.dimensions.component
        val spacing = PdTheme.dimensions.spacing
        val stroke = PdTheme.dimensions.stroke
        val colors = PdTheme.colors
        val track = colors.surface.container
        val work = colors.primary.main
        val warn = colors.status.warning.main
        val line = colors.content.primary
        val knobFill = colors.surface.primary
        val description = PdStringText(R.string.pd_hours_description, listOf(day.summary)).resolve().toString()
        val moment = PdStringText(R.string.pd_hours_now_description, listOf(now.label)).resolve().toString()

        Column(modifier, verticalArrangement = Arrangement.spacedBy(spacing.xxSmall)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(component.timelineHeight)
                    .semantics {
                        contentDescription = description
                        if (scrub.enabled) {
                            stateDescription = moment
                            progressBarRangeInfo = ProgressBarRangeInfo(now.position, 0f..1f)
                            setProgress { value ->
                                scrub.onScrub(value.coerceIn(0f, 1f))
                                true
                            }
                        }
                    }
                    .then(scrubGestures()),
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val width = size.width
                    val trackTop = component.timelineTrackTop.toPx()
                    val trackHeight = component.timelineTrackHeight.toPx()
                    val radius = CornerRadius(trackHeight / 2f)
                    drawRoundRect(track, Offset(0f, trackTop), Size(width, trackHeight), radius)
                    day.segments.forEach { segment ->
                        val left = segment.start * width
                        val right = segment.end * width
                        val shape = Path().apply {
                            addRoundRect(RoundRect(left, trackTop, right, trackTop + trackHeight, radius))
                        }
                        clipPath(shape) {
                            drawRect(work, Offset(left, trackTop), Size(right - left, trackHeight))
                            val warnLeft = segment.warnFrom * width
                            if (warnLeft < right) {
                                drawRect(warn, Offset(warnLeft, trackTop), Size(right - warnLeft, trackHeight))
                            }
                        }
                    }
                    val x = now.position * width
                    val knobCenter = Offset(x, trackTop + trackHeight / 2f)
                    val knobRadius = component.timelineKnobSize.toPx() / 2f
                    val knobStroke = component.timelineKnobStroke.toPx()
                    drawLine(line, Offset(x, component.timelineLineTop.toPx()), knobCenter, stroke.thick.toPx())
                    drawCircle(knobFill, knobRadius, knobCenter)
                    drawCircle(line, knobRadius - knobStroke / 2f, knobCenter, style = Stroke(knobStroke))
                }
                PdBasicText(
                    text = now.label,
                    style = PdTextRole.LabelSmall.style(),
                    color = colors.content.inverse,
                    modifier = Modifier
                        .pdAxisPlacement(now.position)
                        .background(line, PdTheme.shapes.small)
                        .padding(horizontal = spacing.tiny, vertical = spacing.xxSmall),
                    maxLines = 1,
                )
            }
            Box(Modifier.fillMaxWidth()) {
                ticks.forEach { tick ->
                    PdBasicText(
                        text = tick.label,
                        style = PdTextRole.LabelSmall.style(),
                        color = colors.content.tertiary,
                        modifier = Modifier.pdAxisPlacement(tick.position),
                        maxLines = 1,
                    )
                }
            }
        }
    }

    private fun scrubGestures(): Modifier {
        if (!scrub.enabled) return Modifier
        return Modifier
            .pointerInput(scrub) {
                detectTapGestures { offset -> scrub.onScrub((offset.x / size.width).coerceIn(0f, 1f)) }
            }
            .pointerInput(scrub) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    scrub.onScrub((change.position.x / size.width).coerceIn(0f, 1f))
                }
            }
    }
}
