package com.yuriisurzhykov.pointdetector.presentation

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

interface IVibrationService {
    fun vibrate()

    abstract class Abstract constructor(private val applicationContext: Context) :
        IVibrationService {

        abstract fun getVibrator(context: Context): Vibrator

        override fun vibrate() {
            val vibrator = getVibrator(applicationContext)
            vibrator.vibrate(VibrationEffect.createOneShot(40, 200))
        }
    }
}