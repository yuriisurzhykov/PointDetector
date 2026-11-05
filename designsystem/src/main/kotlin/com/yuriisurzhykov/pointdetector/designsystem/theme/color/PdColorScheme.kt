package com.yuriisurzhykov.pointdetector.designsystem.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ColorRole
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ContentColors
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.StatusColors
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.SurfaceColors

@Immutable
data class PdColorScheme(
    val isDark: Boolean,
    val surface: SurfaceColors,
    val content: ContentColors,
    val primary: ColorRole,
    val secondary: ColorRole,
    val status: StatusColors,
    val outline: Color,
    val divider: Color,
)
