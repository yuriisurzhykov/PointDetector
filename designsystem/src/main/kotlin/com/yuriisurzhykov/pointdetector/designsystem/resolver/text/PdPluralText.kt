package com.yuriisurzhykov.pointdetector.designsystem.resolver.text

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource

/** A plurals resource: "1 minute", "2 minutes", "5 minutes" in every supported language. */
@Immutable
data class PdPluralText(
    @param:PluralsRes private val id: Int,
    private val count: Int,
) : PdTextResolver {

    @Composable
    @ReadOnlyComposable
    override fun resolve(): CharSequence = pluralStringResource(id, count, count)
}
