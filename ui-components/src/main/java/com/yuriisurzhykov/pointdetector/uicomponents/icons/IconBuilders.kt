package com.yuriisurzhykov.pointdetector.uicomponents.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdVectorDrawable

// Icon files are asset data: coordinates and stroke widths live here and nowhere else.
// Every icon is drawn in black; the drawing component tints it with a theme colour.

private const val VIEWPORT = 24f
private const val STROKE_WIDTH = 1.9f
private const val STATUS_STROKE_WIDTH = 1.6f
private const val ICON_BOX = 24

private fun vector(name: String, build: ImageVector.Builder.() -> Unit): PdDrawableResolver =
    PdVectorDrawable(
        ImageVector.Builder(
            name = name,
            defaultWidth = ICON_BOX.dp,
            defaultHeight = ICON_BOX.dp,
            viewportWidth = VIEWPORT,
            viewportHeight = VIEWPORT,
        ).apply(build).build(),
    )

internal fun strokeIcon(name: String, vararg paths: String, width: Float = STROKE_WIDTH): PdDrawableResolver =
    vector(name) {
        paths.forEach { d ->
            addPath(
                pathData = addPathNodes(d),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = width,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            )
        }
    }

internal fun fillIcon(name: String, vararg paths: String): PdDrawableResolver =
    vector(name) {
        paths.forEach { d -> addPath(pathData = addPathNodes(d), fill = SolidColor(Color.Black)) }
    }

internal fun ringAndFillIcon(name: String, ring: String, fill: String): PdDrawableResolver =
    vector(name) {
        addPath(
            pathData = addPathNodes(ring),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = STATUS_STROKE_WIDTH,
        )
        addPath(pathData = addPathNodes(fill), fill = SolidColor(Color.Black))
    }

internal fun strokeAndFillIcon(name: String, d: String): PdDrawableResolver =
    vector(name) {
        addPath(
            pathData = addPathNodes(d),
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = STROKE_WIDTH,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
    }
