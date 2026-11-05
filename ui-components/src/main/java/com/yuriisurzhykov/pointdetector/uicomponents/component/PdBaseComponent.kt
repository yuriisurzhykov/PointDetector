package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier

/**
 * Shared skeleton of the implementations (template method). [Content] is closed and wraps
 * every component in the same frame; an implementation only writes [Render].
 * Never use this type in a public signature: take [PdComponent].
 */
@Immutable
abstract class PdBaseComponent : PdComponent {

    @Composable
    final override fun Content(modifier: Modifier) {
        PdComponentFrame { Render(modifier) }
    }

    @Composable
    protected abstract fun Render(modifier: Modifier)
}
