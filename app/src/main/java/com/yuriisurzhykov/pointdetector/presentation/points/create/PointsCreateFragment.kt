package com.yuriisurzhykov.pointdetector.presentation.points.create

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractStyleFragment
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointsListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointsCreateFragment : AbstractStyleFragment(R.layout.fragment_points_create) {

    private val viewModel: PointsCreateViewModel by viewModels(ownerProducer = { requireActivity() })
    private val listAdapter = PointsListAdapter()
    private var navigationCallback: NavigationCallback? = null
    private val pointCreateStateHandler = PointCreateStateHandler()

    override fun getTitle(): CharSequence {
        return resources.getString(R.string.title_points_add_screen)
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
        with(view.findViewById<EditText>(R.id.points_name)) {
            addTextChangedListener { viewModel.updateEnteredPlaceName(it.toString()) }
        }
        with(view.findViewById<RecyclerView>(R.id.recycler)) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        listAdapter.setOnItemClickListener {
            if (it is Point) {
                viewModel.selectPoint(it)
            }
        }
        viewModel.observeCreationState(viewLifecycleOwner) { state ->
            pointCreateStateHandler.handleState(state, this)
        }
        viewModel.observeSuggestedPlaces(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }

}