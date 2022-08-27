package com.yuriisurzhykov.pointdetector.presentation.main

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
        openFragment(PointsListFragment(), "points_list")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (MotionEvent.ACTION_DOWN == ev?.actionMasked) {
            val currentFocusedView = currentFocus
            if (currentFocusedView is EditText) {
                val outRect = Rect()
                currentFocusedView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    currentFocusedView.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}