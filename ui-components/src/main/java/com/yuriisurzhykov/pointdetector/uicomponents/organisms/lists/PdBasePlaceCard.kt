package com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseListItem
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdContentType

@Immutable
abstract class PdBasePlaceCard(
    key: String,
    contentType: PdContentType,
) : PdBaseListItem(key, contentType), PdPlaceCard
