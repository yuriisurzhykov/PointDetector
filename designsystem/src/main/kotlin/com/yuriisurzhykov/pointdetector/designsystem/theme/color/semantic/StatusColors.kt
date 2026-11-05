package com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic

import androidx.compose.runtime.Immutable

/** Semantic states. [info] is the brand blue, so "open" and "information" read as one colour. */
@Immutable
data class StatusColors(
    val info: ColorRole,
    val success: ColorRole,
    val warning: ColorRole,
    val error: ColorRole,
    val neutral: ColorRole,
)
