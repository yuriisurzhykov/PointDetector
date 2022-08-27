package com.yuriisurzhykov.pointdetector.presentation.core

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.yuriisurzhykov.pointdetector.R

abstract class AbstractToolbarActivity : AbstractNavigationActivity() {

    abstract fun onCreated(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreated(savedInstanceState)
        setupToolbar(findViewById(R.id.toolbar))
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