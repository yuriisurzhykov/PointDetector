package com.yuriisurzhykov.pointdetector.toast.ui.animation

import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ColorRole

/**
 * Draws three overlapping gradient layers behind the content: a slow-rotating base sweep,
 * a focal glow and a counter-orbiting accent. The colours come from [role]. With
 * `animated = false` (system "reduce motion") the aurora is drawn once and stays still.
 */
fun Modifier.auroraBackground(role: ColorRole, animated: Boolean): Modifier =
    this then AuroraBackgroundElement(role, animated)
