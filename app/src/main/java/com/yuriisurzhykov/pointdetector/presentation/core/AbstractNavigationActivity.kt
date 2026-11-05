package com.yuriisurzhykov.pointdetector.presentation.core

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yuriisurzhykov.pointdetector.R

abstract class AbstractNavigationActivity : AppCompatActivity(), NavigationCallback {

    /**
     * Closes the activity when the last fragment is on top of the back stack,
     * instead of leaving an empty container. Enabled only in that state so that
     * in all other cases the default back handling pops the fragment.
     */
    private val closeOnLastFragmentCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, closeOnLastFragmentCallback)
        supportFragmentManager.addOnBackStackChangedListener(::updateCloseOnLastFragmentCallback)
        updateCloseOnLastFragmentCallback()
    }

    private fun updateCloseOnLastFragmentCallback() {
        closeOnLastFragmentCallback.isEnabled = supportFragmentManager.backStackEntryCount == 1
    }

    override fun openFragment(fragment: Fragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
        } else {
            supportFragmentManager.popBackStack(tag, 0)
        }
    }

    override fun openFragment(fragment: Fragment, tag: String, removeCurrentFragment: Boolean) {
        if (removeCurrentFragment) {
            supportFragmentManager.popBackStack()
        }
        openFragment(fragment, tag)
    }

    override fun removeCurrentFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}