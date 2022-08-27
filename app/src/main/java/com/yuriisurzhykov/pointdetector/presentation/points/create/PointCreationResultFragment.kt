package com.yuriisurzhykov.pointdetector.presentation.points.create

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractStyleFragment
import java.util.*
import kotlin.concurrent.timerTask

class PointCreationResultFragment : AbstractStyleFragment(R.layout.fragment_point_creation_result) {

    override fun onStart() {
        super.onStart()
        scheduleClosing(2000)
    }

    private fun scheduleClosing(scheduleTime: Long) {
        Timer().schedule(timerTask { openMainFragment() }, scheduleTime)
    }
}