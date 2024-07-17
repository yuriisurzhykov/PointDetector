package com.yuriisurzhykov.pointdetector.presentation.points.create

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractStyleFragment
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.uicomponents.workday.WorkingDaysSelector
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointsAdditionalInfoFragment : AbstractStyleFragment(R.layout.fragment_point_additional_info) {

    private val viewModel: PointsCreateViewModel by viewModels(ownerProducer = { requireActivity() })
    private val pointsCreateStateHandler = PointCreationStateHandle()
    private var workDaySelector: WorkingDaysSelector? = null

    private var locationAddressView: TextView? = null
    private var locationNameView: EditText? = null

    override fun getTitle(): CharSequence {
        return resources.getString(R.string.title_points_add_info_screen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workDaySelector = view.findViewById(R.id.work_days_selector)
        locationAddressView = view.findViewById(R.id.place_address)
        locationNameView = view.findViewById(R.id.place_name_input)

        view.findViewById<View>(R.id.button).setOnClickListener {
            viewModel.savePointWithDays(workDaySelector!!.getSelectedDays())
        }
        view.findViewById<EditText>(R.id.place_name_input).addTextChangedListener {
            viewModel.updateSelectedPlaceName(it.toString())
        }
        viewModel.observeCreationState(viewLifecycleOwner) { state ->
            pointsCreateStateHandler.handleState(state, this)
        }
        viewModel.observeErrorMessage(viewLifecycleOwner) {
            showErrorSnackbar(getString(it))
        }
    }

    private fun updatePointInformation(point: Point) {
        locationAddressView?.text = point.address
        locationNameView?.setText(point.placeName)
    }

    inner class PointCreationStateHandle : PointCreateStateHandler() {
        override fun handleCreateState(point: Point, callback: NavigationCallback) {
            updatePointInformation(point)
        }
    }

    companion object {

        private const val ARG_DATA = "argument_data"

        fun newInstance(point: Point) = PointsAdditionalInfoFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_DATA, point)
            }
        }
    }
}