package com.yuriisurzhykov.pointdetector.presentation.points.create

import android.os.Bundle
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.core.TAG
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractToolbarActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointsCreateActivity : AbstractToolbarActivity() {

    override fun showHomeButtonByDefault() = true

    override fun onCreated(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_singlepane)
        val fragment = PointsCreateFragment()
        openFragment(fragment, fragment.TAG)
    }

    override fun openMainFragment() {
        finish()
    }

}