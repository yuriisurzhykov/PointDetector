package com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent

/**
 * The cards in a lazy column. Every card gives its own key and content type, so rows of one kind
 * reuse each other's composition and a row keeps its identity when the list changes.
 */
@Immutable
class PdCardList(private val cards: List<PdPlaceCard>) : PdBaseComponent(), PdPlaceList {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(start = spacing.small, top = spacing.small, end = spacing.small, bottom = spacing.xLarge),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            items(items = cards, key = { it.key }, contentType = { it.contentType }) { card ->
                card.Content(Modifier)
            }
        }
    }
}
