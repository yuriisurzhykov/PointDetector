package com.yuriisurzhykov.pointdetector.presentation.vibration

import android.content.Context
import android.os.Vibrator
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService

class VibratorServiceApi29(appContext: Context) : IVibrationService.Abstract(appContext) {

    override fun getVibrator(context: Context): Vibrator {
        return context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}