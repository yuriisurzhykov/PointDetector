package com.yuriisurzhykov.pointsdetector.uicomponents

import android.view.View

val View.density: Float
    get() = context.resources.displayMetrics.density