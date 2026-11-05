package com.yuriisurzhykov.pointdetector.designsystem.theme.color

import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ColorRole
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ContentColors
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.MapColors
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.StatusColors
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.SurfaceColors

private val LightPrimary = ColorRole(
    main = Palette.Blue500,
    onMain = Palette.White,
    container = Palette.Blue100,
    onContainer = Palette.Blue900,
)

val PdLightColorScheme = PdColorScheme(
    isDark = false,
    surface = SurfaceColors(
        background = Palette.Slate25,
        primary = Palette.White,
        container = Palette.Slate50,
        scrim = Palette.Scrim,
    ),
    content = ContentColors(
        primary = Palette.Ink,
        secondary = Palette.Slate600,
        tertiary = Palette.Slate500,
        inverse = Palette.White,
    ),
    primary = LightPrimary,
    secondary = ColorRole(
        main = Palette.Blue300,
        onMain = Palette.Blue950,
        container = Palette.Blue50,
        onContainer = Palette.Blue900,
    ),
    status = StatusColors(
        info = LightPrimary,
        success = ColorRole(
            main = Palette.Green600,
            onMain = Palette.White,
            container = Palette.Green50,
            onContainer = Palette.Green900,
        ),
        warning = ColorRole(
            main = Palette.Amber700,
            onMain = Palette.White,
            container = Palette.Amber50,
            onContainer = Palette.Amber900,
        ),
        error = ColorRole(
            main = Palette.Red600,
            onMain = Palette.White,
            container = Palette.Red50,
            onContainer = Palette.Red900,
        ),
        neutral = ColorRole(
            main = Palette.Slate400,
            onMain = Palette.White,
            container = Palette.NeutralDay,
            onContainer = Palette.NeutralDayText,
        ),
    ),
    outline = Palette.Slate200,
    divider = Palette.Slate100,
    map = MapColors(
        land = Palette.MapLandDay,
        water = Palette.MapWaterDay,
        park = Palette.MapParkDay,
        road = Palette.White,
        edge = Palette.MapEdgeDay,
    ),
)
