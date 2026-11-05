package com.yuriisurzhykov.pointdetector.designsystem.preview.atoms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.atoms.feedback.PdArcSpinner
import com.yuriisurzhykov.pointdetector.designsystem.atoms.feedback.PdSkeletonBlock
import com.yuriisurzhykov.pointdetector.designsystem.atoms.feedback.PdSkeletonSize
import com.yuriisurzhykov.pointdetector.designsystem.atoms.feedback.PdSkeletonWidth
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewRow
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone

@PdPreviews
@Composable
private fun PdArcSpinnerPreview() {
    PdPreviewFrame {
        PdPreviewLabel("tones")
        PdPreviewRow {
            listOf(PdContentTone.Brand, PdContentTone.Primary, PdContentTone.Secondary).forEach {
                PdArcSpinner(it).Content(Modifier)
            }
        }
    }
}

@PdPreviews
@Composable
private fun PdSkeletonBlockPreview() {
    PdPreviewFrame {
        PdSkeletonSize.entries.forEach { size ->
            PdPreviewLabel(size.name)
            PdSkeletonWidth.entries.forEach { width ->
                PdSkeletonBlock(size, width).Content(Modifier)
            }
        }
    }
}
