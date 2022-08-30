package com.yuriisurzhykov.pointdetector.presentation.main

import android.os.Bundle
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractToolbarActivity
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointsListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AbstractToolbarActivity(), NavigationCallback {

    override fun showHomeButtonByDefault() = false

    override fun onCreated(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_singlepane)
        openMainFragment()
    }

    override fun openMainFragment() {
        openFragment(PointsListFragment(), "points_list")
    }
}