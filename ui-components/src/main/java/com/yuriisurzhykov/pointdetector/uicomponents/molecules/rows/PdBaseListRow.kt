package com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * The shared layout of a row: [leading], a title with an optional [subtitle], and [trailing].
 * A subclass decides what the leading and trailing parts are and how a tap is announced.
 */
@Immutable
abstract class PdBaseListRow(
    private val title: PdTextResolver,
    private val subtitle: PdComponent,
    private val selected: Boolean,
    private val onClick: () -> Unit,
) : PdBaseComponent(), PdListRow {

    protected open val leading: PdComponent get() = PdNoComponent
    protected open val trailing: PdComponent get() = PdNoComponent
    protected open val role: Role get() = Role.Button

    @Composable
    final override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val colors = PdTheme.colors
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(role = role, onClick = onClick)
                .heightIn(min = PdTheme.dimensions.component.rowMinHeight)
                .padding(horizontal = spacing.regular, vertical = spacing.small),
            horizontalArrangement = Arrangement.spacedBy(spacing.regular),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leading.Content(Modifier)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(spacing.xxSmall)) {
                PdBasicText(
                    text = title,
                    style = PdTextRole.TitleMedium.style(),
                    color = if (selected) colors.primary.main else colors.content.primary,
                    maxLines = 1,
                )
                subtitle.Content(Modifier)
            }
            trailing.Content(Modifier)
        }
    }
}
