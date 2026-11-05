package com.yuriisurzhykov.pointdetector.presentation.points.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractFragment
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMapFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Hosts the place screen. The screen is Compose ([PlaceScreen]); this fragment keeps what belongs
 * to the platform: the route and share intents, the shared toolbar and the back stack.
 */
@AndroidEntryPoint
class PointDetailsFragment : AbstractFragment() {

    private val viewModel: PointDetailsViewModel by viewModels()

    @Inject
    lateinit var mapFactory: PdMapFactory

    @Inject
    lateinit var mapper: PlaceDetailsMapper

    private val actions = object : PlaceDetailsActions {
        override fun onBack() {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        override fun onShare() {
            val send = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, viewModel.point.value.address)
                type = "text/plain"
            }
            startSafely(Intent.createChooser(send, null))
        }

        override fun onRoute() {
            val point = viewModel.point.value
            val where = "${point.coordinates.lat},${point.coordinates.lng}"
            val label = Uri.encode(point.placeName)
            startSafely(Intent(Intent.ACTION_VIEW, Uri.parse("geo:$where?q=$where($label)")))
        }

        override fun onFavorite() = viewModel.onFavorite()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            PdTheme {
                val point by viewModel.point.collectAsStateWithLifecycle()
                PlaceScreen(point, actions, mapFactory, mapper)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        toolbar()?.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        toolbar()?.visibility = View.VISIBLE
    }

    /** There may be no app that can show a route or share text; that is not a reason to crash. */
    private fun startSafely(intent: Intent) {
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            // Nothing can handle it on this phone.
        }
    }

    /** The screen draws its own back button under the system bars, so the shared toolbar is hidden. */
    private fun toolbar(): View? = activity?.findViewById(R.id.toolbar)

    companion object {
        fun newInstance(point: PointUi) = PointDetailsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(PointDetailsViewModel.ARG_ENTITY, point)
            }
        }
    }
}
