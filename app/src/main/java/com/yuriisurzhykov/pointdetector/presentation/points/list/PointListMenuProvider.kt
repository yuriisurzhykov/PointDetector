package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.yuriisurzhykov.pointdetector.R

class PointListMenuProvider(private val onCreateClick: () -> Unit) : MenuProvider {
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.points_list_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.add_point) {
            onCreateClick.invoke()
            return true
        }
        return false
    }
}