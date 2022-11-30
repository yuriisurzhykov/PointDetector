package com.yuriisurzhykov.pointdetector.presentation.core

import android.app.Activity
import android.view.View
import java.lang.ref.SoftReference

abstract class AbstractActivityClickListener constructor(
    activity: Activity
) : View.OnClickListener {

    private val activityRef = SoftReference(activity)

    protected fun getActivity() = activityRef.get()

}