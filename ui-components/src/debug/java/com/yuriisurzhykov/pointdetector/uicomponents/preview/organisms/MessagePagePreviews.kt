package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIconIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.snackbar.PdMessageSnackbar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.message.PdMessagePage
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

private val MESSAGE_PAGE_HEIGHT = 640.dp
private const val RATIONALE = "Location permissions are needed so the app can work properly. Without them it is not possible to calculate the distance to the places."

@PdPreviews
@Composable
private fun PdMessagePagePreview() {
    PdTheme {
        var denied by remember { mutableStateOf(false) }
        val footer = if (denied) {
            listOf(
                PdMessageSnackbar(pdText("To grant the permission, open the settings.")),
                PdPrimaryButton(pdText("Open settings")) {},
                PdOutlineButton(pdText("Continue anyway")) {},
            )
        } else {
            listOf(
                PdPrimaryButton(pdText("Grant the permission")) { denied = true },
                PdOutlineButton(pdText("Continue anyway")) {},
            )
        }
        PdMessagePage(
            illustration = PdIconIllustration(if (denied) PdIcons.Warning else PdIcons.Locate),
            title = pdText(if (denied) "Could not get the permission" else "No access to the location"),
            message = pdText(RATIONALE),
            footer = footer,
        ).Content(Modifier.fillMaxWidth().height(MESSAGE_PAGE_HEIGHT))
    }
}
