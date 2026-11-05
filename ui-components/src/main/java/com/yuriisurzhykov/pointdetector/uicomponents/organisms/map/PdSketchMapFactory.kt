package com.yuriisurzhykov.pointdetector.uicomponents.organisms.map

import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapScene

/** The factory of [PdSketchMap]: the default until a real map module is bound. */
object PdSketchMapFactory : PdMapFactory {
    override fun create(scene: PdMapScene): PdMap = PdSketchMap(scene)
}
