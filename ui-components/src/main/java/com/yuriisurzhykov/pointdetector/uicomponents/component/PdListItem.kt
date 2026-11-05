package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.runtime.Stable

/**
 * A block that lives in a lazy list. [key] is a String because list keys must survive
 * saving to a Bundle; [contentType] lets the list reuse compositions between similar rows.
 */
@Stable
interface PdListItem : PdComponent {
    val key: String
    val contentType: PdContentType
}
