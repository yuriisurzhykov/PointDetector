package com.yuriisurzhykov.pointdetector.map.maplibre

import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapScene
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMap
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMapFactory

/**
 * The free map: MapLibre over OpenFreeMap tiles (OpenStreetMap data). The app binds this class to
 * [PdMapFactory] in one place; nothing else mentions MapLibre.
 */
class MapLibreMapFactory : PdMapFactory {
    override fun create(scene: PdMapScene): PdMap = PdMapLibre(scene)
}
