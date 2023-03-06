package com.yuriisurzhykov.pointdetector.presentation.favorites

import com.yuriisurzhykov.pointdetector.presentation.IVibrationService

interface SwipeVibrateManager : OnSwipeActionControl {

    abstract class Abstract(
        private val vibrateDxThreshold: Int,
        private val action: () -> Unit
    ) : SwipeVibrateManager {

        private var wasVibration = false

        override fun tryCallOnSwipe(dX: Float) {
            if (dX < 2f) {
                wasVibration = false
            }
            if (dX > vibrateDxThreshold && !wasVibration) {
                wasVibration = true
                action.invoke()
            }
        }
    }

    class VibrateAction(
        private val vibrateService: IVibrationService,
        vibrateDxThreshold: Int
    ) : Abstract(vibrateDxThreshold, action = {
        vibrateService.vibrate()
    })

    class CallbackAction(vibrateDxThreshold: Int, callback: () -> Unit) :
        Abstract(vibrateDxThreshold, callback)
}