package com.yuriisurzhykov.pointdetector.toast.tokens

/**
 * Tunables of the toast effects: the aurora background and the pull-reveal clip. This is a
 * token file, the only place in the module where such numbers may appear; the code that
 * uses them refers to the names. Sizes and colours still come from `PdTheme`.
 */
internal object ToastTokens {

    // Aurora: co-prime periods keep the three waves from locking into a visible loop.
    const val PHASE_A_MS = 7_000
    const val PHASE_B_MS = 5_300
    const val PHASE_C_MS = 3_700

    const val BASE_ANGLE_DEG = 35f
    const val BASE_ANGLE_SWING_DEG = 25f
    const val GLOW_MIX = 0.5f

    const val FOCAL_ALPHA = 0.55f
    const val FOCAL_X = 0.35f
    const val FOCAL_X_SWING = 0.25f
    const val FOCAL_Y = 0.40f
    const val FOCAL_Y_SWING = 0.20f
    const val FOCAL_RADIUS = 0.55f

    const val ACCENT_ALPHA = 0.35f
    const val ACCENT_X = 0.65f
    const val ACCENT_X_SWING = 0.20f
    const val ACCENT_Y = 0.55f
    const val ACCENT_Y_SWING = 0.25f
    const val ACCENT_RADIUS = 0.45f

    // Pull reveal: the centre of the bottom edge drops first, the corners follow.
    const val REVEAL_CENTER_MS = 400
    const val REVEAL_EDGE_MS = 350
    const val REVEAL_STAGGER_MS = 150
    const val BEZIER_NEAR = 0.75f
    const val BEZIER_FAR = 0.25f

    // Swipe to dismiss
    const val SWIPE_ALPHA_REDUCTION = 0.5f
}
