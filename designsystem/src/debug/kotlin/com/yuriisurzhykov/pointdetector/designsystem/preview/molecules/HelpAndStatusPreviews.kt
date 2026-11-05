package com.yuriisurzhykov.pointdetector.designsystem.preview.molecules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.icons.PdIcons
import com.yuriisurzhykov.pointdetector.designsystem.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.designsystem.molecules.help.PdFactItem
import com.yuriisurzhykov.pointdetector.designsystem.molecules.status.PdHintedStatusLine
import com.yuriisurzhykov.pointdetector.designsystem.molecules.status.PdSimpleStatusLine
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.designsystem.preview.pdText
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdEmphasisText

@PdPreviews
@Composable
private fun PdFactItemPreview() {
    PdPreviewFrame {
        PdFactItem(
            icon = PdIcons.Calendar,
            label = pdText("Appointment"),
            value = pdText("Required: call a day ahead"),
        ).Content(Modifier)
        PdFactItem(
            icon = PdIcons.Id,
            label = pdText("Documents"),
            value = pdText("Not required"),
        ).Content(Modifier)
        PdFactItem(
            icon = PdIcons.Truck,
            label = pdText("Delivery"),
            value = pdText("Available for people who cannot travel"),
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdSimpleStatusLinePreview() {
    PdPreviewFrame {
        PdSimpleStatusLine(PlaceStatus.Open, pdText("Open now")).Content(Modifier)
        PdSimpleStatusLine(PlaceStatus.ClosingSoon, pdText("Closing soon")).Content(Modifier)
        PdSimpleStatusLine(PlaceStatus.Closed, pdText("Closed")).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdHintedStatusLinePreview() {
    PdPreviewFrame {
        PdPreviewLabel("the hint sits under the title, aligned with the text")
        PdHintedStatusLine(
            status = PlaceStatus.Open,
            title = PdEmphasisText(pdText("Open until 6 PM"), pdText("6 PM")),
            hint = pdText("Next opening: tomorrow at 9 AM"),
        ).Content(Modifier)
        PdHintedStatusLine(
            status = PlaceStatus.Closed,
            title = pdText("Closed"),
            hint = pdText("Opens on Tuesday at 10 AM"),
        ).Content(Modifier)
    }
}
