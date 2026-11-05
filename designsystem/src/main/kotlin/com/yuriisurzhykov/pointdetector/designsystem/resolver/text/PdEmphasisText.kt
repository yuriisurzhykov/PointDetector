package com.yuriisurzhykov.pointdetector.designsystem.resolver.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.buildAnnotatedString
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/**
 * Emphasises one fragment of [base] and returns an `AnnotatedString`, e.g. "Closes in **40 min**".
 * The emphasis style is a typography token, never a literal weight.
 */
@Immutable
data class PdEmphasisText(
    private val base: PdTextResolver,
    private val emphasized: PdTextResolver,
) : PdTextResolver {

    @Composable
    @ReadOnlyComposable
    override fun resolve(): CharSequence {
        val style = PdTheme.typography.titleMedium.toSpanStyle()
        val whole = base.resolve().toString()
        val fragment = emphasized.resolve().toString()
        val start = whole.indexOf(fragment)
        return buildAnnotatedString {
            append(whole)
            if (start >= 0) addStyle(style, start, start + fragment.length)
        }
    }
}
