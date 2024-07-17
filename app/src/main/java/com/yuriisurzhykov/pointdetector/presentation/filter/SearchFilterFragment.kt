package com.yuriisurzhykov.pointdetector.presentation.filter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractStyleFragment
import com.yuriisurzhykov.pointdetector.presentation.delegate.findView
import com.yuriisurzhykov.pointdetector.presentation.list.RadioGroupAdapter
import com.yuriisurzhykov.pointdetector.presentation.list.SelectOptionsRadioButtonInflater
import com.yuriisurzhykov.pointdetector.uicomponents.textselect.SelectOption
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFilterFragment : AbstractStyleFragment(R.layout.fragment_search_filter) {

    private val sortingTypeSelect: RadioGroup by findView(R.id.select_sorting_type)
    private val measureTypeSelect: RadioGroup by findView(R.id.select_measure_type)
    private val applyButton: Button by findView(R.id.apply_button)
    private val measureTypesAdapter = RadioGroupAdapter(SelectOptionsRadioButtonInflater())
    private val sortTypesAdapter = RadioGroupAdapter(SelectOptionsRadioButtonInflater())
    private val viewModel: SearchFilterViewModel by viewModels()

    override fun getTitle() = resources.getString(R.string.title_filter_screen)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUiData()
        viewModel.observeMeasureTypes(viewLifecycleOwner) {
            measureTypesAdapter.submitList(it)
        }
        viewModel.observeSortingTypes(viewLifecycleOwner) {
            sortTypesAdapter.submitList(it)
        }
        viewModel.observeConfigsChanged(viewLifecycleOwner) { finished ->
            if (finished) {
                removeCurrentFragment()
            }
        }
        measureTypesAdapter.attachToGroup(measureTypeSelect)
        sortTypesAdapter.attachToGroup(sortingTypeSelect)
        applyButton.setOnClickListener(applyClickListener)
    }

    private val applyClickListener = View.OnClickListener {
        val measureConf =
            measureTypeSelect.getChildAt(measureTypeSelect.checkedRadioButtonId - 1).tag as SelectOption
        val sortConf =
            sortingTypeSelect.getChildAt(sortingTypeSelect.checkedRadioButtonId - 1).tag as SelectOption
        viewModel.updateConfigs(measureConf, sortConf)
    }

}