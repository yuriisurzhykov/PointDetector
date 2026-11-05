package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.city.CityPickerDialog
import com.yuriisurzhykov.pointdetector.presentation.city.CityViewModel
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.delegate.findView
import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.filter.SearchFilterFragment
import com.yuriisurzhykov.pointdetector.presentation.map.AbstractLocationFragment
import com.yuriisurzhykov.pointdetector.presentation.points.create.PointsCreateActivity
import com.yuriisurzhykov.pointdetector.presentation.points.details.PointDetailsFragment
import com.yuriisurzhykov.pointdetector.presentation.vibration.VibrationService
import com.yuriisurzhykov.pointdetector.uicomponents.list.EmptyStateData
import com.yuriisurzhykov.pointdetector.uicomponents.list.LayoutManagerFactory
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.swipe.SwipeEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointsListFragment : AbstractLocationFragment(R.layout.fragment_points_list) {

    private val viewModel: PointsListViewModel by viewModels()
    private val cityViewModel: CityViewModel by viewModels()
    private val listAdapter = PointsListAdapter()
    private var navigationCallback: NavigationCallback? = null
    private var shownCityCode: String? = null
    private var isCityShown = false

    private val filterButton: TextView by findView(R.id.search_filter)
    private val cityButton: TextView by findView(R.id.city_selector)
    private val fragmentMenuProvider = PointListMenuProvider {
        onCreateNewPointClick()
    }

    override fun getTitle() = resources.getString(R.string.title_points_list_screen)

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
            layoutManager = LayoutManagerFactory().produce(view.context)
            val swipes = PointSwipeActions(view.context, VibrationService(), listAdapter)
            SwipeEngine.Base(
                listOf(
                    swipes.favorite { viewModel.toggleFavorite(it) },
                    swipes.deleteUserPoint { viewModel.removeUserPoint(it) }
                )
            ).attachTo(this)
        }
        with(view.findViewById<EditText>(R.id.search_text_input)) {
            setText(viewModel.getSearchCondition())
            addTextChangedListener { viewModel.startLoadPoints(it.toString()) }
        }
        filterButton.setOnClickListener(filterClickListener)
        cityButton.setOnClickListener {
            CityPickerDialog().show(childFragmentManager, CityPickerDialog.TAG)
        }
        cityViewModel.observeSelectedCity(viewLifecycleOwner) { onCityChanged(it) }
        activity?.addMenuProvider(fragmentMenuProvider, viewLifecycleOwner)
        listAdapter.setOnItemClickListener { item: ViewHolderItem ->
            if (item is EmptyStateData) {
                onCreateNewPointClick()
            } else if (item is PointUi) {
                openPointDetailsFragment(item)
            }
        }
        viewModel.observePointsList(viewLifecycleOwner) {
            this@PointsListFragment.view?.post { listAdapter.submitList(it) }
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
        cityViewModel.refresh()
    }

    /**
     * The points belong to the city, so a different city (chosen by the user or detected
     * later) means different points. The first value is shown by the usual loading of the list.
     */
    private fun onCityChanged(city: CityUi?) {
        cityButton.text = city?.label ?: getString(R.string.city_not_selected)
        val changed = isCityShown && shownCityCode != city?.code
        isCityShown = true
        shownCityCode = city?.code
        if (changed) {
            viewModel.startLoadPoints(viewModel.getSearchCondition().toString())
        }
    }

    private fun openPointDetailsFragment(item: PointUi) {
        openFragment(PointDetailsFragment.newInstance(item), "point_details_stack_name")
    }

    private val filterClickListener = View.OnClickListener {
        openFragment(SearchFilterFragment(), "filters_screen")
    }

}