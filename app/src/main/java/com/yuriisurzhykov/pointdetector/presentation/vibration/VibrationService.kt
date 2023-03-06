package com.yuriisurzhykov.pointdetector.presentation.vibration

import android.content.Context
import android.os.Build
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService
import javax.inject.Inject

class VibrationService @Inject constructor(private val appContext: Context) : IVibrationService {
    override fun vibrate() {
        val service = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorServiceApi30(appContext)
        } else {
            VibratorServiceApi29(appContext)
        }
        service.vibrate()
    }
}