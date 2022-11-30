package com.yuriisurzhykov.pointdetector.presentation.points.details

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.delegate.findView
import com.yuriisurzhykov.pointdetector.presentation.delegate.serializableArgument
import com.yuriisurzhykov.pointdetector.presentation.map.AbstractLocationFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointDetailsFragment : AbstractLocationFragment(R.layout.fragment_point_details) {

    private val viewModel: PointDetailsViewModel by viewModels()
    private val pointInfo: Point by serializableArgument(ARG_ENTITY)

    private val pointName: TextView by findView(R.id.point_name_view)
    private val pointAddress: TextView by findView(R.id.point_address_view)
    private val buildRouteButton: Button by findView(R.id.button_build_route)
    private val shareAddressButton: Button by findView(R.id.button_share_address)
    private val workingHoursRecycler: RecyclerView by findView(R.id.working_hours_recycler)

    override fun getTitle(): CharSequence = getString(R.string.title_point_details)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pointName.text = pointInfo.placeName
        pointAddress.text = pointInfo.address
        pointAddress.setOnClickListener(activity?.let { activity ->
            BuildRouteClickListener(pointInfo, activity)
        })
        buildRouteButton.setOnClickListener(activity?.let { activity ->
            BuildRouteClickListener(pointInfo, activity)
        })
        shareAddressButton.setOnClickListener(activity?.let { activity ->
            ShareRouteClickListener(pointInfo, activity)
        })
        workingHoursRecycler.layoutManager = LinearLayoutManager(view.context)
        workingHoursRecycler.adapter = WorkingHoursListAdapter().also { adapter ->
            adapter.submitList(pointInfo.workingHours)
        }
    }

    companion object {
        private const val ARG_ENTITY = "argument_name"

        fun newInstance(point: Point) = PointDetailsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_ENTITY, point)
            }
        }
    }

}