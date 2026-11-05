package com.yuriisurzhykov.pointdetector.presentation.permissions

/** Which of the two permission screens is shown. */
enum class PermissionStep {

    /** The system can still ask: the main button opens its dialog. */
    Request,

    /** The system will not ask again: the main button leads to the settings of the app. */
    Settings;

    companion object {

        /** The step after a refusal; [shouldShowRationale] is false once the system stopped asking. */
        fun afterDenial(shouldShowRationale: Boolean): PermissionStep = if (shouldShowRationale) Request else Settings
    }
}
