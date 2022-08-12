package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.map.AbstractLocationFragment
import com.yuriisurzhykov.pointdetector.presentation.points.create.PointsCreateFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException

@AndroidEntryPoint
class PointsListFragment : AbstractLocationFragment(R.layout.fragment_points_list) {

    private val viewModel: PointsListViewModel by viewModels()
    private val listAdapter = PointsListAdapter()
    private var navigationCallback: NavigationCallback? = null

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
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
        with(view.findViewById<View>(R.id.add_point_button)) {
            setOnClickListener {
                navigationCallback?.openFragment(PointsCreateFragment(), "create_point_fragment")
            }
        }
        listAdapter.setOnItemClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${it.address}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        viewModel.observePointsList(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        viewModel.startLoadPoints()
    }

    override fun onLocationReceived(location: Location) {
        viewModel.updateUserLocation(location)
    }

}