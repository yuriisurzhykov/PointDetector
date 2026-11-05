package com.yuriisurzhykov.pointdetector.presentation.city

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.yuriisurzhykov.pointdetector.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Lets the user choose a city by hand. The choice is shared with the screen that opened
 * the dialog through the view model of that screen.
 */
@AndroidEntryPoint
class CityPickerDialog : DialogFragment() {

    private val viewModel: CityViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cities = viewModel.availableCities()
        val builder = AlertDialog.Builder(requireActivity())
            .setTitle(R.string.title_dialog_choose_city)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
        if (cities.isEmpty()) {
            return builder.setMessage(R.string.message_cities_not_loaded).create()
        }
        val checked = cities.indexOfFirst { it.code == viewModel.selectedCityCode() }
        return builder
            .setSingleChoiceItems(cities.map { it.label }.toTypedArray(), checked) { dialog, which ->
                viewModel.select(cities[which])
                dialog.dismiss()
            }
            .create()
    }

    companion object {
        const val TAG = "city_picker_dialog"
    }
}
