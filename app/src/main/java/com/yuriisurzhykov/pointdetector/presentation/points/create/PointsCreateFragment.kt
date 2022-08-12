package com.yuriisurzhykov.pointdetector.presentation.points.create

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException

@AndroidEntryPoint
class PointsCreateFragment : Fragment(R.layout.fragment_points_create) {

    private val viewModel: PointsCreateViewModel by viewModels()
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
        with(view.findViewById<EditText>(R.id.points_name)) {
            addTextChangedListener { viewModel.updateEnteredPlaceName(it.toString()) }
        }
        with(view.findViewById<RecyclerView>(R.id.recycler)) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        listAdapter.setOnItemClickListener {
            viewModel.savePoint(it)
        }
        viewModel.observeSavedState(viewLifecycleOwner) {
            navigationCallback?.removeCurrentFragment() ?: activity?.supportFragmentManager?.popBackStack()
        }
        viewModel.observeSuggestedPlaces(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }

}