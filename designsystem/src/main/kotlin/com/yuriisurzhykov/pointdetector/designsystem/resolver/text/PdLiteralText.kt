package com.yuriisurzhykov.pointdetector.designsystem.resolver.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable

/** Text that already exists as data: a place name, an address, a time prepared by a mapper. */
@Immutable
class PdLiteralText(private val value: CharSequence) : PdTextResolver {

    @Composable
    @ReadOnlyComposable
    override fun resolve(): CharSequence = value

    override fun equals(other: Any?): Boolean = other is PdLiteralText && other.value.toString() == value.toString()

    override fun hashCode(): Int = value.toString().hashCode()
}
