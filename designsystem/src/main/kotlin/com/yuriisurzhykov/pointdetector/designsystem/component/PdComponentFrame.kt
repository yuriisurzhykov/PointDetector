package com.yuriisurzhykov.pointdetector.designsystem.component

import androidx.compose.runtime.Composable

/**
 * The one place that wraps every component. Cross-cutting concerns (test tags,
 * tracing, accessibility defaults) are added here once instead of in each implementation.
 */
@Composable
internal fun PdComponentFrame(content: @Composable () -> Unit) {
    content()
}
