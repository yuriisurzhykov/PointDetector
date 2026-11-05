package com.yuriisurzhykov.pointdetector.presentation.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIconIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.snackbar.PdMessageSnackbar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.message.PdMessagePage
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPage
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** Makes the page of a [PermissionStep]. Kept apart from the screen so the host can ask for it too. */
object PermissionPages {

    fun page(step: PermissionStep, actions: PermissionActions): PdPage {
        val skip = PdOutlineButton(PdStringText(R.string.button_continue_anyway), onClick = actions::onContinue)
        return when (step) {
            PermissionStep.Request -> PdMessagePage(
                illustration = PdIconIllustration(PdIcons.Locate),
                title = PdStringText(R.string.title_location_permission_request),
                message = PdStringText(R.string.rationale_location_permission_text),
                footer = listOf(
                    PdPrimaryButton(PdStringText(R.string.button_location_permissions_retry), onClick = actions::onGrant),
                    skip,
                ),
            )

            PermissionStep.Settings -> PdMessagePage(
                illustration = PdIconIllustration(PdIcons.Warning),
                title = PdStringText(R.string.title_location_permission_denied),
                message = PdStringText(R.string.rationale_location_permission_text),
                footer = listOf(
                    PdMessageSnackbar(PdStringText(R.string.snackbar_location_permissions_denied)),
                    PdPrimaryButton(
                        PdStringText(R.string.button_provide_location_permissions_never_ask),
                        onClick = actions::onOpenSettings,
                    ),
                    skip,
                ),
            )
        }
    }
}

/** The permission screen of the first start: asks for the location or leads to the settings. */
@Composable
fun PermissionScreen(step: PermissionStep, actions: PermissionActions) {
    remember(step, actions) { PermissionPages.page(step, actions) }.Content(Modifier)
}
