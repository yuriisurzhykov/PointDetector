package com.yuriisurzhykov.pointdetector.presentation.vibration

import android.content.Context
import android.os.Vibrator
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService

class VibratorServiceApi29 : IVibrationService.Abstract() {

    override fun getVibrator(context: Context): Vibrator {
        return context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}