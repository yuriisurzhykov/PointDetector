package com.yuriisurzhykov.pointdetector.presentation

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

interface IVibrationService {
    fun vibrate(context: Context)

    abstract class Abstract : IVibrationService {

        abstract fun getVibrator(context: Context): Vibrator

        override fun vibrate(context: Context) {
            val vibrator = getVibrator(context)
            vibrator.vibrate(VibrationEffect.createOneShot(40, 200))
        }
    }
}