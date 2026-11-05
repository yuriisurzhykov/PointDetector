package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.presentation.city.CityPickerActions
import com.yuriisurzhykov.pointdetector.presentation.city.CityPickerSheet
import com.yuriisurzhykov.pointdetector.presentation.city.CityViewModel
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.filter.FiltersActions
import com.yuriisurzhykov.pointdetector.presentation.filter.FiltersSheet
import com.yuriisurzhykov.pointdetector.presentation.filter.FiltersViewModel
import com.yuriisurzhykov.pointdetector.presentation.map.AbstractLocationFragment
import com.yuriisurzhykov.pointdetector.presentation.places.PlaceCardMapper
import com.yuriisurzhykov.pointdetector.presentation.places.PlaceStatusMapper
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesActions
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesOverlay
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesScreen
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesTab
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesViewModel
import com.yuriisurzhykov.pointdetector.presentation.points.create.PointsCreateActivity
import com.yuriisurzhykov.pointdetector.presentation.points.details.PointDetailsFragment
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMapFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Hosts the main screen. The screen itself is Compose ([PlacesScreen]) and so are the city and
 * filters sheets over it; this fragment keeps what still belongs to the platform: the permission
 * and the location, and the screens that are not redesigned yet.
 */
@AndroidEntryPoint
class PointsListFragment : AbstractLocationFragment() {

    private val viewModel: PlacesViewModel by viewModels()
    private val cityViewModel: CityViewModel by viewModels()
    private val filtersViewModel: FiltersViewModel by viewModels()

    @Inject
    lateinit var mapFactory: PdMapFactory

    @Inject
    lateinit var cardMapper: PlaceCardMapper

    @Inject
    lateinit var statusMapper: PlaceStatusMapper

    private var navigationCallback: NavigationCallback? = null

    private val actions = object : PlacesActions {
        override fun onOpenPlace(point: PointUi) {
            openFragment(PointDetailsFragment.newInstance(point), "point_details_stack_name")
        }

        override fun onFavorite(point: PointUi) = viewModel.onFavorite(point)
        override fun onSelect(pointId: Long) = viewModel.onSelect(pointId)
        override fun onTab(tab: PlacesTab) = viewModel.onTab(tab)
        override fun onQuery(text: String) = viewModel.onQuery(text)
        override fun onSearching(searching: Boolean) = viewModel.onSearching(searching)
        override fun onSheet(state: PdSheetState) = viewModel.onSheet(state)
        override fun onLocate() = viewModel.onLocate()

        override fun onFilters() {
            filtersViewModel.load()
            viewModel.onOverlay(PlacesOverlay.Filters)
        }

        override fun onCity() = viewModel.onOverlay(PlacesOverlay.City)

        override fun onAdd() {
            startActivity(Intent(activity, PointsCreateActivity::class.java))
        }
    }

    private val cityActions = object : CityPickerActions {
        override fun onSelect(city: CityUi) = cityViewModel.select(city)
        override fun onDetect() = cityViewModel.detect()
        override fun onRetry() = cityViewModel.retry()
        override fun onClose() = viewModel.onOverlay(PlacesOverlay.None)
    }

    private val filtersActions = object : FiltersActions {
        override fun onSort(id: String) = filtersViewModel.selectSort(id)
        override fun onUnit(id: String) = filtersViewModel.selectUnit(id)
        override fun onReset() = filtersViewModel.reset()
        override fun onApply() = filtersViewModel.apply()
        override fun onClose() = viewModel.onOverlay(PlacesOverlay.None)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            PdTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                PlacesScreen(state, actions, mapFactory, cardMapper, statusMapper)
                BackHandler(enabled = state.overlay != PlacesOverlay.None) { viewModel.onOverlay(PlacesOverlay.None) }
                when (state.overlay) {
                    PlacesOverlay.City -> {
                        val list by cityViewModel.list.collectAsStateWithLifecycle()
                        val selected by cityViewModel.selected.collectAsStateWithLifecycle()
                        val failed by cityViewModel.detectionFailed.collectAsStateWithLifecycle()
                        CityPickerSheet(list, selected?.code, failed, cityActions)
                    }

                    PlacesOverlay.Filters -> {
                        val filters by filtersViewModel.state.collectAsStateWithLifecycle()
                        FiltersSheet(filters, filtersActions) {
                            viewModel.onOverlay(PlacesOverlay.None)
                            viewModel.reload()
                        }
                    }

                    PlacesOverlay.None -> Unit
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cityViewModel.selected.collect { viewModel.onCityChanged(it) }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        toolbar()?.visibility = View.GONE
        checkPermissions()
        viewModel.reload()
    }

    override fun onStop() {
        super.onStop()
        toolbar()?.visibility = View.VISIBLE
    }

    override fun onLocationReceived(location: Location) {
        viewModel.updateUserLocation(location)
        cityViewModel.refresh()
    }

    /** The screen draws its own bars under the system ones, so the shared toolbar is hidden here. */
    private fun toolbar(): View? = activity?.findViewById(R.id.toolbar)
}
