package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.presentation.city.CityPickerDialog
import com.yuriisurzhykov.pointdetector.presentation.city.CityViewModel
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.filter.SearchFilterFragment
import com.yuriisurzhykov.pointdetector.presentation.map.AbstractLocationFragment
import com.yuriisurzhykov.pointdetector.presentation.places.PlaceCardMapper
import com.yuriisurzhykov.pointdetector.presentation.places.PlaceStatusMapper
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesActions
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesScreen
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesTab
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesViewModel
import com.yuriisurzhykov.pointdetector.presentation.points.create.PointsCreateActivity
import com.yuriisurzhykov.pointdetector.presentation.points.details.PointDetailsFragment
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMapFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Hosts the main screen. The screen itself is Compose ([PlacesScreen]); this fragment keeps what
 * still belongs to the platform: the permission and the location, the city picker, the other
 * screens that are not redesigned yet.
 */
@AndroidEntryPoint
class PointsListFragment : AbstractLocationFragment() {

    private val viewModel: PlacesViewModel by viewModels()
    private val cityViewModel: CityViewModel by viewModels()

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
        override fun onFilters() = openFragment(SearchFilterFragment(), "filters_screen")

        override fun onCity() {
            CityPickerDialog().show(childFragmentManager, CityPickerDialog.TAG)
        }

        override fun onAdd() {
            startActivity(Intent(activity, PointsCreateActivity::class.java))
        }
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
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityViewModel.observeSelectedCity(viewLifecycleOwner) { viewModel.onCityChanged(it) }
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
