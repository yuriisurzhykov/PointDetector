package com.yuriisurzhykov.pointdetector.presentation.favorites

import android.content.Context
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService

interface SwipeVibrateManager {

    fun tryVibrateOnDraw(dx: Float, context: Context)

    abstract class Abstract(
        private val vibrateService: IVibrationService,
        private val vibrateDxThreshold: Int
    ) : SwipeVibrateManager {

        private var wasVibration = false

        override fun tryVibrateOnDraw(dx: Float, context: Context) {
            if (dx < 2f) {
                wasVibration = false
            }
            if (dx > vibrateDxThreshold && !wasVibration) {
                wasVibration = true
                vibrateService.vibrate(context)
            }
        }
    }

    class Base(vibrateService: IVibrationService, vibrateDxThreshold: Int) :
        Abstract(vibrateService, vibrateDxThreshold)
}