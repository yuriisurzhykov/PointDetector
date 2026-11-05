package com.yuriisurzhykov.pointdetector.toast.ui.animation

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ColorRole

internal data class AuroraBackgroundElement(
    private val role: ColorRole,
    private val animated: Boolean,
) : ModifierNodeElement<AuroraBackgroundNode>() {

    override fun create(): AuroraBackgroundNode = AuroraBackgroundNode(role, animated)

    override fun update(node: AuroraBackgroundNode) {
        node.update(role, animated)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "auroraBackground"
        properties["role"] = role
        properties["animated"] = animated
    }
}
