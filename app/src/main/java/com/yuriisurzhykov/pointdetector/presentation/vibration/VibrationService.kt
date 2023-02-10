package com.yuriisurzhykov.pointdetector.presentation.vibration

import android.content.Context
import android.os.Build
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService

class VibrationService : IVibrationService {
    override fun vibrate(context: Context) {
        val service = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorServiceApi30()
        } else {
            VibratorServiceApi29()
        }
        service.vibrate(context)
    }
}