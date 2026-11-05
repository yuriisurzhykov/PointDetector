package com.yuriisurzhykov.pointdetector.presentation.points.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.main.UiDependencies

/**
 * The place screen with what belongs to the platform: the route and share intents. Going back is
 * left to the caller, the heart is kept by the view model of this place.
 */
@Composable
fun PlaceRoute(
    point: PointUi,
    ui: UiDependencies,
    onBack: () -> Unit,
    viewModel: PointDetailsViewModel = hiltViewModel(key = "place-${point.id}"),
) {
    val context = LocalContext.current
    LaunchedEffect(point.id) { viewModel.show(point) }
    val current by viewModel.point.collectAsStateWithLifecycle()
    val shown = current ?: point
    val actions = remember(viewModel, onBack, context) { placeActions(context, viewModel, onBack) }
    PlaceScreen(shown, actions, ui.mapFactory, ui.details)
}

private fun placeActions(context: Context, viewModel: PointDetailsViewModel, onBack: () -> Unit) = object : PlaceDetailsActions {

    override fun onBack() = onBack()

    override fun onShare() {
        val point = viewModel.point.value ?: return
        val send = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, point.address)
            type = "text/plain"
        }
        startSafely(Intent.createChooser(send, null))
    }

    override fun onRoute() {
        val point = viewModel.point.value ?: return
        val where = "${point.coordinates.lat},${point.coordinates.lng}"
        val label = Uri.encode(point.placeName)
        startSafely(Intent(Intent.ACTION_VIEW, Uri.parse("geo:$where?q=$where($label)")))
    }

    override fun onFavorite() = viewModel.onFavorite()

    /** There may be no app that can show a route or share text; that is not a reason to crash. */
    private fun startSafely(intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            // Nothing can handle it on this phone.
        }
    }
}
