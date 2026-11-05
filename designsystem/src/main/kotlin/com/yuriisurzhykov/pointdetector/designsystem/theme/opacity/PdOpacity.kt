package com.yuriisurzhykov.pointdetector.designsystem.theme.opacity

import androidx.compose.runtime.Immutable

@Immutable
data class PdOpacity(
    val disabled: Float,
    val subtle: Float,
    val dim: Float,
    val halo: Float,
)

val DefaultOpacity = PdOpacity(
    disabled = 0.4f,
    subtle = 0.12f,
    dim = 0.5f,
    halo = 0.16f,
)
