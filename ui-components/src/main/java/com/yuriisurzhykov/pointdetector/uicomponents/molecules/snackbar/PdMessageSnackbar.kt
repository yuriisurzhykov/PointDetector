package com.yuriisurzhykov.pointdetector.uicomponents.molecules.snackbar

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A message and nothing to press. */
@Immutable
class PdMessageSnackbar(message: PdTextResolver) : PdBaseSnackbar(message)
