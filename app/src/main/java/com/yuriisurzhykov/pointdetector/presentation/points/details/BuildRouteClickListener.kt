package com.yuriisurzhykov.pointdetector.presentation.points.details

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractActivityClickListener

class BuildRouteClickListener constructor(
    private val point: Point,
    activity: Activity
): AbstractActivityClickListener(activity) {

    override fun onClick(v: View) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${point.address}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        getActivity()?.startActivity(mapIntent)
    }

}