package com.yuriisurzhykov.pointdetector.presentation.core

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yuriisurzhykov.pointdetector.R

abstract class AbstractNavigationActivity : AppCompatActivity(), NavigationCallback {

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
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}