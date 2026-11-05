package com.yuriisurzhykov.pointdetector.uicomponents.component

/**
 * Content type of a list row, passed to `LazyColumn(contentType = ...)` so that rows of one
 * kind reuse each other's composition. A value class over a string: modules add their own
 * types without editing this file.
 */
@JvmInline
value class PdContentType(val id: String) {

    companion object {
        val PlaceCard = PdContentType("place-card")
        val PlaceCardLoading = PdContentType("place-card-loading")
    }
}
