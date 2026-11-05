package com.yuriisurzhykov.pointdetector.uicomponents.organisms.map

import androidx.compose.runtime.Stable
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent

/**
 * A map. The UI depends only on this contract; which library draws it is decided by the
 * [PdMapFactory] that is bound in the app.
 */
@Stable
interface PdMap : PdComponent
