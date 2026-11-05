package com.yuriisurzhykov.pointdetector.designsystem.theme.color

import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ColorRole
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ContentColors
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.MapColors
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.StatusColors
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.SurfaceColors

/**
 * At night the primary lightens to `#75A4FE`: the project's night accent `#053898` has a
 * contrast of 1.4 : 1 against its own night surface and is practically invisible.
 */
private val DarkPrimary = ColorRole(
    main = Palette.Blue300,
    onMain = Palette.Blue950,
    container = Palette.Blue800,
    onContainer = Palette.Blue200,
)

val PdDarkColorScheme = PdColorScheme(
    isDark = true,
    surface = SurfaceColors(
        background = Palette.Night900,
        primary = Palette.Night800,
        container = Palette.Night700,
        scrim = Palette.Scrim,
    ),
    content = ContentColors(
        primary = Palette.NightText,
        secondary = Palette.NightTextSecondary,
        tertiary = Palette.NightTextTertiary,
        inverse = Palette.Ink,
    ),
    primary = DarkPrimary,
    secondary = ColorRole(
        main = Palette.Blue400,
        onMain = Palette.Blue950,
        container = Palette.Blue850,
        onContainer = Palette.Blue100,
    ),
    status = StatusColors(
        info = DarkPrimary,
        success = ColorRole(
            main = Palette.Green400,
            onMain = Palette.Green950,
            container = Palette.GreenNight,
            onContainer = Palette.GreenNightText,
        ),
        warning = ColorRole(
            main = Palette.Amber400,
            onMain = Palette.Amber950,
            container = Palette.AmberNight,
            onContainer = Palette.AmberNightText,
        ),
        error = ColorRole(
            main = Palette.Red300,
            onMain = Palette.Red950,
            container = Palette.RedNight,
            onContainer = Palette.RedNightText,
        ),
        neutral = ColorRole(
            main = Palette.NightTextTertiary,
            onMain = Palette.NeutralNightOn,
            container = Palette.Night700,
            onContainer = Palette.NeutralNightText,
        ),
    ),
    outline = Palette.Night600,
    divider = Palette.Night700,
    map = MapColors(
        land = Palette.MapLandNight,
        water = Palette.MapWaterNight,
        park = Palette.MapParkNight,
        road = Palette.MapRoadNight,
        edge = Palette.MapEdgeNight,
    ),
)
