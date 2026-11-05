package com.yuriisurzhykov.pointdetector.designsystem.organisms.lists

import androidx.compose.runtime.Stable
import com.yuriisurzhykov.pointdetector.designsystem.component.PdListItem

/** A place in a list. Screens hold `List<PdPlaceCard>` and never ask which class is inside. */
@Stable
interface PdPlaceCard : PdListItem
