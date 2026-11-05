package com.yuriisurzhykov.pointdetector.presentation.permissions

import org.junit.Assert.assertEquals
import org.junit.Test

class PermissionStepTest {

    @Test
    fun `test a refusal the system may ask about again keeps the request screen`() {
        assertEquals(PermissionStep.Request, PermissionStep.afterDenial(shouldShowRationale = true))
    }

    @Test
    fun `test a refusal after which the system is silent leads to the settings`() {
        assertEquals(PermissionStep.Settings, PermissionStep.afterDenial(shouldShowRationale = false))
    }
}
