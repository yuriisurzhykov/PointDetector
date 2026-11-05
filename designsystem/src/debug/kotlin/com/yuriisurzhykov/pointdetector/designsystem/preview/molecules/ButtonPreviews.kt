package com.yuriisurzhykov.pointdetector.designsystem.preview.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.designsystem.icons.PdIcons
import com.yuriisurzhykov.pointdetector.designsystem.molecules.buttons.PdDisabledButton
import com.yuriisurzhykov.pointdetector.designsystem.molecules.buttons.PdLoadingButton
import com.yuriisurzhykov.pointdetector.designsystem.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.designsystem.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.designsystem.preview.pdText
import kotlinx.coroutines.delay

private const val LOADING_DEMO_MILLIS = 1_500L

@PdPreviews
@Composable
private fun PdPrimaryButtonPreview() {
    PdPreviewFrame {
        var clicks by remember { mutableIntStateOf(0) }
        PdPreviewLabel("press and release: the button shrinks on a spring")
        PdPrimaryButton(
            text = pdText("Get directions ($clicks)"),
            icon = PdDecorativeIcon(PdIcons.Route, PdIconSize.Regular),
            onClick = { clicks++ },
        ).Content(Modifier)
        PdPrimaryButton(text = pdText("Without an icon"), onClick = { clicks++ }).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdOutlineButtonPreview() {
    PdPreviewFrame {
        var clicks by remember { mutableIntStateOf(0) }
        PdOutlineButton(
            text = pdText("Call the place ($clicks)"),
            icon = PdDecorativeIcon(PdIcons.Info, PdIconSize.Regular),
            onClick = { clicks++ },
        ).Content(Modifier)
        PdOutlineButton(text = pdText("Without an icon"), onClick = { clicks++ }).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdLoadingButtonPreview() {
    PdPreviewFrame {
        var loading by remember { mutableStateOf(false) }
        LaunchedEffect(loading) {
            if (loading) {
                delay(LOADING_DEMO_MILLIS)
                loading = false
            }
        }
        PdPreviewLabel("always loading: it has no click handler at all")
        PdLoadingButton(pdText("Saving")).Content(Modifier)
        PdPreviewLabel("tap: loading for a moment, then back to the primary button")
        if (loading) {
            PdLoadingButton(pdText("Saving")).Content(Modifier)
        } else {
            PdPrimaryButton(text = pdText("Save"), onClick = { loading = true }).Content(Modifier)
        }
    }
}

@PdPreviews
@Composable
private fun PdDisabledButtonPreview() {
    PdPreviewFrame {
        PdPreviewLabel("not clickable by construction, announced as disabled")
        PdDisabledButton(pdText("Unavailable")).Content(Modifier)
    }
}
