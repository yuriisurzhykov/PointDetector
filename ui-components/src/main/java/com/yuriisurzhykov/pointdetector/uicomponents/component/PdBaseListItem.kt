package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.runtime.Immutable

@Immutable
abstract class PdBaseListItem(
    final override val key: String,
    final override val contentType: PdContentType,
) : PdBaseComponent(), PdListItem
