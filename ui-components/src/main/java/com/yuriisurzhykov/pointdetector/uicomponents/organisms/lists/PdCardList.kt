package com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdListScroll
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdNoScroll

/**
 * The cards in a lazy column. Every card gives its own key and content type, so rows of one kind
 * reuse each other's composition and a row keeps its identity when the list changes.
 */
@Immutable
class PdCardList(
    private val cards: List<PdPlaceCard>,
    private val scroll: PdListScroll = PdNoScroll,
) : PdBaseComponent(), PdPlaceList {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val listState = rememberLazyListState()
        // An order is carried out once. The cards change often (the clock, a heart, a new position of
        // the phone) and must not pull the list back to the selected card while it is being browsed.
        val latestCards by rememberUpdatedState(cards)
        LaunchedEffect(scroll) {
            val index = scroll.indexIn(latestCards.map { it.key })
            if (index >= 0) listState.animateScrollToItem(index)
        }
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(start = spacing.small, top = spacing.small, end = spacing.small, bottom = spacing.xLarge),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            items(items = cards, key = { it.key }, contentType = { it.contentType }) { card ->
                card.Content(Modifier)
            }
        }
    }
}
