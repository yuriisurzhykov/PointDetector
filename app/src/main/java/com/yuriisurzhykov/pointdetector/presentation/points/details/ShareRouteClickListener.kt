package com.yuriisurzhykov.pointdetector.presentation.points.details

import android.app.Activity
import android.content.Intent
import android.view.View
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractActivityClickListener

class ShareRouteClickListener constructor(private val point: Point, activity: Activity) :
    AbstractActivityClickListener(activity) {

    override fun onClick(v: View?) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, point.address)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        getActivity()?.startActivity(shareIntent)
    }
}