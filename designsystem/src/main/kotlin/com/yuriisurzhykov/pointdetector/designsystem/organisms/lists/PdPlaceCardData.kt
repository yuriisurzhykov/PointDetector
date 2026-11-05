package com.yuriisurzhykov.pointdetector.designsystem.organisms.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.atoms.status.PdCircleNumberMark
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.style
import com.yuriisurzhykov.pointdetector.designsystem.component.PdContentType
import com.yuriisurzhykov.pointdetector.designsystem.component.pdPressable
import com.yuriisurzhykov.pointdetector.designsystem.molecules.status.PdStatusLine
import com.yuriisurzhykov.pointdetector.designsystem.molecules.tags.PdTag
import com.yuriisurzhykov.pointdetector.designsystem.molecules.toggles.PdFavoriteToggle
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/**
 * A place with data. Every part that can vary is itself a component: the status line, the
 * tags, the favourite toggle. The card knows none of their classes, only their interfaces.
 */
@Immutable
class PdPlaceCardData(
    key: String,
    private val number: Int,
    private val name: PdTextResolver,
    private val address: PdTextResolver,
    private val distance: PdTextResolver,
    private val status: PdStatusLine,
    private val tags: List<PdTag>,
    private val favorite: PdFavoriteToggle,
    private val selected: Boolean,
    private val onOpen: () -> Unit,
) : PdBasePlaceCard(key, PdContentType.PlaceCard) {

    @Composable
    override fun Render(modifier: Modifier) {
        val colors = PdTheme.colors
        val dimensions = PdTheme.dimensions
        val shape = PdTheme.shapes.medium
        val frame = if (selected) colors.primary.main else colors.divider
        val frameWidth = if (selected) dimensions.stroke.thick else dimensions.stroke.thin
        val numberMark = remember(number, selected) { PdCircleNumberMark(number, selected) }

        Column(
            modifier = modifier
                .pdPressable(Role.Button, onOpen)
                .fillMaxWidth()
                .clip(shape)
                .background(colors.surface.primary)
                .border(frameWidth, frame, shape)
                .padding(
                    start = dimensions.spacing.regular,
                    top = dimensions.spacing.small,
                    end = dimensions.spacing.tiny,
                    bottom = dimensions.spacing.regular,
                ),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.small),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                numberMark.Content(Modifier)
                PdBasicText(
                    text = name,
                    style = PdTextRole.HeadingSmall.style(),
                    color = colors.content.primary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                )
                favorite.Content(Modifier)
            }
            Row(
                modifier = Modifier.padding(end = dimensions.spacing.small),
                horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.tiny),
            ) {
                PdBasicText(
                    text = address,
                    style = PdTextRole.BodySmall.style(),
                    color = colors.content.secondary,
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 1,
                )
                PdBasicText(distance, PdTextRole.BodySmall.style(), colors.content.secondary, maxLines = 1)
            }
            status.Content(Modifier.padding(end = dimensions.spacing.small))
            if (tags.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.tiny)) {
                    tags.forEach { it.Content(Modifier) }
                }
            }
        }
    }
}
