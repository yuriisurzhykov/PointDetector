package com.yuriisurzhykov.pointdetector.designsystem.organisms.lists

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.designsystem.atoms.feedback.PdSkeletonBlock
import com.yuriisurzhykov.pointdetector.designsystem.atoms.feedback.PdSkeletonSize
import com.yuriisurzhykov.pointdetector.designsystem.atoms.feedback.PdSkeletonWidth
import com.yuriisurzhykov.pointdetector.designsystem.component.PdContentType
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** A place that is still loading. No data: a skeleton of the same shape as the real card. */
@Immutable
class PdPlaceCardLoading(key: String) : PdBasePlaceCard(key, PdContentType.PlaceCardLoading) {

    @Composable
    override fun Render(modifier: Modifier) {
        val shape = PdTheme.shapes.medium
        val spacing = PdTheme.dimensions.spacing
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .border(PdTheme.dimensions.stroke.thin, PdTheme.colors.divider, shape)
                .padding(spacing.regular),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            PdSkeletonBlock(PdSkeletonSize.Title, PdSkeletonWidth.Wide).Content(Modifier)
            PdSkeletonBlock(PdSkeletonSize.Line, PdSkeletonWidth.Narrow).Content(Modifier)
            PdSkeletonBlock(PdSkeletonSize.Chip, PdSkeletonWidth.Wide).Content(Modifier)
        }
    }
}
