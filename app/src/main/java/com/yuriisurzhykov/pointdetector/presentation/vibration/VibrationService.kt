package com.yuriisurzhykov.pointdetector.presentation.vibration

import android.content.Context
import android.os.Build
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService

class VibrationService : IVibrationService {
    private val service by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorServiceApi30()
        } else {
            VibratorServiceApi29()
        }
    }
    override fun vibrate(context: Context) {
        service.vibrate(context)
    }
}