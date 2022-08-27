package com.yuriisurzhykov.pointdetector.presentation.core

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.yuriisurzhykov.pointdetector.R

abstract class AbstractToolbarActivity : AbstractNavigationActivity() {

    abstract fun onCreated(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreated(savedInstanceState)
        setupToolbar(findViewById(R.id.toolbar))
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

    abstract fun showHomeButtonByDefault(): Boolean

    protected fun setupToolbar(toolbar: Toolbar?) {
        setSupportActionBar(toolbar)
        manageToolbarTitle()
        manageToolbarIcon()
        supportFragmentManager.addOnBackStackChangedListener {
            manageToolbarTitle()
            manageToolbarIcon()
        }
    }

    private fun manageToolbarIcon() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(showHomeButtonByDefault())
        }
    }

    private fun manageToolbarTitle() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.content_container)
        if (currentFragment is IStyledFragment) {
            supportActionBar?.title = currentFragment.getTitle()
        }
    }
}