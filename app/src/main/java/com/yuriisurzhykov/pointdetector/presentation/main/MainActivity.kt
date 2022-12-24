package com.yuriisurzhykov.pointdetector.presentation.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractToolbarActivity
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointsListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AbstractToolbarActivity(), NavigationCallback {

    private val viewModel: MainViewModel by viewModels()
    private var importMenuItem: MenuItem? = null

    private val menuItemsProvider: MenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_import_data, menu)
            importMenuItem = menu.findItem(R.id.menu_item_import_data)
            importMenuItem?.isVisible = viewModel.isImportEnabled()
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.menu_item_import_data) {
                viewModel.importData()
                return true
            }
            return false
        }
    }

    override fun showHomeButtonByDefault() = false

    override fun onCreated(savedInstanceState: Bundle?) {
        addMenuProvider(menuItemsProvider, this, Lifecycle.State.RESUMED)
        setContentView(R.layout.activity_singlepane)
        openMainFragment()
        viewModel.observeImportOptionVisibility(this) { isImportEnabled ->
            importMenuItem?.isVisible = isImportEnabled
        }
    }

    override fun openMainFragment() {
        openFragment(PointsListFragment(), "points_list")
    }
}