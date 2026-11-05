package com.yuriisurzhykov.pointdetector.designsystem.organisms.lists

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseListItem
import com.yuriisurzhykov.pointdetector.designsystem.component.PdContentType

@Immutable
abstract class PdBasePlaceCard(
    key: String,
    contentType: PdContentType,
) : PdBaseListItem(key, contentType), PdPlaceCard
