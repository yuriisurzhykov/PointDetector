package com.yuriisurzhykov.pointdetector.uicomponents.preview.resolver

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewRow
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdEmphasisText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdPluralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

private const val ICONS_PER_ROW = 4
private const val MAX_MINUTES = 5

@PdPreviews
@Composable
private fun PdLiteralTextPreview() {
    PdPreviewFrame {
        PdPreviewLabel("text that already exists as data")
        PdPlainText(pdText("Sacramento Food Bank, 1200 Market Street")).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdEmphasisTextPreview() {
    PdPreviewFrame {
        PdPreviewLabel("one fragment is emphasised with a typography token")
        PdPlainText(PdEmphasisText(pdText("Closes in 40 min"), pdText("40 min"))).Content(Modifier)
        PdPlainText(PdEmphasisText(pdText("Open until 6 PM, closes at 6 PM"), pdText("6 PM"))).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdStringTextPreview() {
    PdPreviewFrame {
        PdPreviewLabel("a string resource whose arguments are resolvers too")
        PdPlainText(PdStringText(R.string.pd_preview_greeting, listOf(pdText("PointDetector")))).Content(Modifier)
        PdPlainText(
            PdStringText(R.string.pd_preview_closes_at, listOf(pdText("Pantry"), pdText("6 PM"))),
            PdTextRole.TitleMedium,
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdPluralTextPreview() {
    PdPreviewFrame {
        var minutes by remember { mutableIntStateOf(1) }
        PdPreviewLabel("tap the button to change the count: 1 minute, 2 minutes ...")
        PdPlainText(PdPluralText(R.plurals.pd_preview_minutes, minutes), PdTextRole.TitleMedium).Content(Modifier)
        PdOutlineButton(
            text = pdText("Next count"),
            onClick = { minutes = minutes % MAX_MINUTES + 1 },
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdVectorDrawableIconCatalogPreview() {
    PdPreviewFrame {
        PdPreviewLabel("every icon of PdIcons, each one is a PdVectorDrawable")
        listOf(
            "Heart" to PdIcons.Heart,
            "HeartFilled" to PdIcons.HeartFilled,
            "Route" to PdIcons.Route,
            "Close" to PdIcons.Close,
            "Check" to PdIcons.Check,
            "Info" to PdIcons.Info,
            "Warning" to PdIcons.Warning,
            "Error" to PdIcons.Error,
            "Calendar" to PdIcons.Calendar,
            "Id" to PdIcons.Id,
            "Truck" to PdIcons.Truck,
            "StatusOpen" to PdIcons.StatusOpen,
            "StatusClosingSoon" to PdIcons.StatusClosingSoon,
            "StatusClosed" to PdIcons.StatusClosed,
        ).chunked(ICONS_PER_ROW).forEach { row ->
            PdPreviewRow {
                row.forEach { (name, icon) ->
                    PdPreviewIconCell(name, icon)
                }
            }
        }
    }
}

@Composable
private fun PdPreviewIconCell(name: String, icon: PdDrawableResolver) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PdDecorativeIcon(icon, PdIconSize.Large).Content(Modifier)
        PdPlainText(pdText(name), PdTextRole.LabelSmall).Content(Modifier)
    }
}
