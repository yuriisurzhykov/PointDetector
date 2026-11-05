package com.yuriisurzhykov.pointdetector.designsystem.resolver.text

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

/** A string resource. Arguments are resolvers too, so they are translated and formatted together. */
@Immutable
data class PdStringText(
    @param:StringRes private val id: Int,
    private val args: List<PdTextResolver> = emptyList(),
) : PdTextResolver {

    @Composable
    @ReadOnlyComposable
    override fun resolve(): CharSequence {
        val formatArgs = args.map { it.resolve().toString() }.toTypedArray()
        return stringResource(id, *formatArgs)
    }
}
