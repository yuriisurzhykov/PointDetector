package com.yuriisurzhykov.pointdetector.presentation.points.details

/** What the person can do on the place screen. The screen only reports; the host decides. */
interface PlaceDetailsActions {
    fun onBack()
    fun onShare()
    fun onRoute()
    fun onFavorite()
}
