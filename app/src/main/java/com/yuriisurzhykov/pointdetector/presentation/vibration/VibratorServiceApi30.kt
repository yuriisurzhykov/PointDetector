package com.yuriisurzhykov.pointdetector.presentation.vibration

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import com.yuriisurzhykov.pointdetector.presentation.IVibrationService

@RequiresApi(Build.VERSION_CODES.S)
class VibratorServiceApi30 : IVibrationService.Abstract() {

    override fun getVibrator(context: Context): Vibrator {
        return (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    }
}