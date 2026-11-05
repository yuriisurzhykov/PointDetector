package com.yuriisurzhykov.pointdetector.uicomponents.organisms.map

import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapScene

/** Makes a map for a scene. Swapping the map implementation means binding another factory. */
interface PdMapFactory {
    fun create(scene: PdMapScene): PdMap
}
