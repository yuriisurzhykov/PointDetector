package com.yuriisurzhykov.pointdetector.presentation.core

import androidx.fragment.app.Fragment

interface NavigationCallback {

    fun openFragment(fragment: Fragment, tag: String)

    fun removeCurrentFragment()

}