package com.yuriisurzhykov.pointdetector.designsystem.theme.color

import androidx.compose.ui.graphics.Color

/**
 * Raw colour values. Internal on purpose: components never see a palette entry, they ask
 * the theme for a semantic role. The blues are the project's own: accent_color `#1B69FD`
 * (colors.xml) and `#75A4FE` / `#E8F0FE` (ic_location.xml).
 */
internal object Palette {

    // Brand blue
    val Blue50 = Color(0xFFE8F0FE)
    val Blue100 = Color(0xFFDCE8FF)
    val Blue200 = Color(0xFFD6E4FF)
    val Blue300 = Color(0xFF75A4FE)
    val Blue400 = Color(0xFF9CC0FF)
    val Blue500 = Color(0xFF1B69FD)
    val Blue800 = Color(0xFF1F3F73)
    val Blue850 = Color(0xFF24406B)
    val Blue900 = Color(0xFF0A2A66)
    val Blue950 = Color(0xFF04193F)

    // Cool neutrals, tinted towards the brand blue
    val Slate25 = Color(0xFFF2F6FE)
    val Slate50 = Color(0xFFEDF2FA)
    val Slate100 = Color(0xFFD0DAEA)
    val Slate200 = Color(0xFFB8C4D9)
    val Slate400 = Color(0xFF66707F)
    val Slate500 = Color(0xFF5F6F88)
    val Slate600 = Color(0xFF4A5A73)
    val Ink = Color(0xFF030D20)
    val White = Color.White

    // Dark surfaces: the project's night card colour and its neighbours
    val Night900 = Color(0xFF121D29)
    val Night800 = Color(0xFF1C2C3A)
    val Night700 = Color(0xFF25384D)
    val Night600 = Color(0xFF34475C)
    val NightText = Color(0xFFF0F0F0)
    val NightTextSecondary = Color(0xFFA9B7CB)
    val NightTextTertiary = Color(0xFF8C9BB0)

    // Status hues
    val Green600 = Color(0xFF17794A)
    val Green50 = Color(0xFFD6F0E0)
    val Green900 = Color(0xFF0B3A22)
    val Green400 = Color(0xFF5FD39A)
    val Green950 = Color(0xFF04281A)
    val GreenNight = Color(0xFF123D2B)
    val GreenNightText = Color(0xFFBFF0D8)

    val Amber700 = Color(0xFF9A6400)
    val Amber50 = Color(0xFFFFF1D6)
    val Amber900 = Color(0xFF5A3A00)
    val Amber400 = Color(0xFFE5B85C)
    val Amber950 = Color(0xFF2E1E00)
    val AmberNight = Color(0xFF4A3A12)
    val AmberNightText = Color(0xFFF7E2B0)

    val Red600 = Color(0xFFC62828)
    val Red50 = Color(0xFFFDE2E2)
    val Red900 = Color(0xFF7A1010)
    val Red300 = Color(0xFFFF8A80)
    val Red950 = Color(0xFF3B0A07)
    val RedNight = Color(0xFF5C1E1E)
    val RedNightText = Color(0xFFFFD6D2)

    val NeutralDay = Color(0xFFE6EAF0)
    val NeutralDayText = Color(0xFF2D3643)
    val NeutralNightText = Color(0xFFD5DEE9)
    val NeutralNightOn = Color(0xFF0F1620)

    val Scrim = Color(0x52000000)
}
