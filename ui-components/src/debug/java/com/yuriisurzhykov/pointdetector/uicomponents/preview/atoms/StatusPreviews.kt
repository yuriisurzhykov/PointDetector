package com.yuriisurzhykov.pointdetector.uicomponents.preview.atoms

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdCircleNumberMark
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdPlaceStatusMark
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewRow
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

@PdPreviews
@Composable
private fun PdPlaceStatusMarkPreview() {
    PdPreviewFrame {
        PdPreviewLabel("filled, half and empty circle: the state never rests on colour alone")
        listOf(
            "Open" to PlaceStatus.Open,
            "Closing soon" to PlaceStatus.ClosingSoon,
            "Closed" to PlaceStatus.Closed,
        ).forEach { (name, status) ->
            PdPreviewRow {
                PdPlaceStatusMark(status).Content(Modifier)
                PdPlainText(pdText(name)).Content(Modifier)
            }
        }
    }
}

@PdPreviews
@Composable
private fun PdCircleNumberMarkPreview() {
    PdPreviewFrame {
        var selected by remember { mutableStateOf(false) }
        PdPreviewLabel("tap the third number to select it")
        PdPreviewRow {
            PdCircleNumberMark(number = 1, selected = false).Content(Modifier)
            PdCircleNumberMark(number = 2, selected = true).Content(Modifier)
            PdCircleNumberMark(number = 3, selected = selected)
                .Content(Modifier.clickable { selected = !selected })
        }
    }
}
