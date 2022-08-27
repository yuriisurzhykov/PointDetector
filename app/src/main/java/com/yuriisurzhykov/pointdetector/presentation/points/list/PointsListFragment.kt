package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.core.TAG
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.map.AbstractLocationFragment
import com.yuriisurzhykov.pointdetector.presentation.points.create.PointsCreateActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.IllegalStateException

@AndroidEntryPoint
class PointsListFragment : AbstractLocationFragment(R.layout.fragment_points_list) {

    private val viewModel: PointsListViewModel by viewModels()
    private val listAdapter = PointsListAdapter()
    private var navigationCallback: NavigationCallback? = null

    override fun getTitle(): CharSequence {
        return resources.getString(R.string.title_points_list_screen)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is NavigationCallback) {
            throw IllegalStateException("Context must implement NavigationCallback")
        }
        navigationCallback = context
    }

    override fun onDetach() {
        super.onDetach()
        navigationCallback = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.findViewById<RecyclerView>(R.id.recycler)) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            ItemTouchHelper(PointSwipeDeleteCallback(listAdapter, view.context)).attachToRecyclerView(this)
        }
        with(view.findViewById<EditText>(R.id.search_text_input)) {
            setText(viewModel.getSearchCondition())
            addTextChangedListener { viewModel.startLoadPoints(it.toString()) }
        }
        activity?.addMenuProvider(fragmentMenuProvider, viewLifecycleOwner)
        listAdapter.setOnItemClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${it.address}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        listAdapter.setOnRemoveClickListener { point, _ ->
            viewModel.removeItem(point)
        }
        viewModel.observePointsList(viewLifecycleOwner) {
            listAdapter.submitList(it)
            Log.e(TAG, "onViewCreated: ${Json.encodeToString(it)}")
        }
    }

    private fun onCreateNewPointClick() {
        startActivity(Intent(activity, PointsCreateActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        viewModel.startLoadPoints()
    }

    override fun onLocationReceived(location: Location) {
        viewModel.updateUserLocation(location)
    }

    private val fragmentMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.points_list_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.add_point) {
                onCreateNewPointClick()
                return true
            }
            return false
        }
    }

}