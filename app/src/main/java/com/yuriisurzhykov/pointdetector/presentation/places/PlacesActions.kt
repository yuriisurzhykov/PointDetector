package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState

/** What the person can do on the main screen. The screen only reports; the host decides. */
interface PlacesActions {
    fun onOpenPlace(point: PointUi)
    fun onFavorite(point: PointUi)
    fun onSelect(pointId: Long)
    fun onTab(tab: PlacesTab)
    fun onQuery(text: String)
    fun onSearching(searching: Boolean)
    fun onSheet(state: PdSheetState)
    fun onLocate()
    fun onFilters()
    fun onCity()
    fun onAdd()
}
