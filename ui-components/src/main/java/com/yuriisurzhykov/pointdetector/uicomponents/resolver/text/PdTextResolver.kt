package com.yuriisurzhykov.pointdetector.uicomponents.resolver.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable

/**
 * Any visible text. Components never take a raw `String` or a `@StringRes`: they take a
 * resolver and call [resolve] where the text is drawn. It returns a [CharSequence] so an
 * implementation can hand back an `AnnotatedString` with spans.
 */
@Stable
interface PdTextResolver {

    @Composable
    @ReadOnlyComposable
    fun resolve(): CharSequence
}
