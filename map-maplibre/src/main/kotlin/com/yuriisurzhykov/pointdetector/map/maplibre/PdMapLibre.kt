package com.yuriisurzhykov.pointdetector.map.maplibre

import android.graphics.PointF
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdUserDot
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdOverlayAt
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdGeoPoint
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapCamera
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapScene
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMap
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import kotlin.math.max

private const val CITY_ZOOM = 12.0
private const val WORLD_ZOOM = 2.0
private const val FOCUS_ZOOM = 14.0

/**
 * The map drawn by MapLibre over the free OpenFreeMap tiles. The markers and the user dot are the
 * design system's own components, laid over the map view at the projected positions, so a pin
 * looks the same on any map implementation.
 */
@Immutable
internal class PdMapLibre(private val scene: PdMapScene) : PdBaseComponent(), PdMap {

    @Composable
    override fun Render(modifier: Modifier) {
        val context = LocalContext.current
        val density = LocalDensity.current
        val colors = PdTheme.colors
        val spacing = PdTheme.dimensions.spacing
        val styleJson = remember(colors.map, colors.content.secondary) {
            OpenFreeMapStyle.json(
                MapStyleColors(
                    land = colors.map.land.toArgb(),
                    water = colors.map.water.toArgb(),
                    park = colors.map.park.toArgb(),
                    road = colors.map.road.toArgb(),
                    edge = colors.map.edge.toArgb(),
                    label = colors.content.secondary.toArgb(),
                    labelHalo = colors.map.land.toArgb(),
                ),
            )
        }
        val mapView = remember {
            MapLibre.getInstance(context)
            MapView(context).apply { onCreate(null) }
        }
        ManageLifecycle(mapView)

        var map by remember { mutableStateOf<MapLibreMap?>(null) }
        val cameraTick = remember { mutableIntStateOf(0) }
        val coveredPx = with(density) { scene.coveredBottom.roundToPx() }

        Box(modifier.fillMaxSize().background(colors.map.land)) {
            AndroidView(
                factory = {
                    mapView.also { view ->
                        view.getMapAsync { ready ->
                            ready.uiSettings.apply {
                                isLogoEnabled = false
                                isAttributionEnabled = false
                                isCompassEnabled = false
                                isRotateGesturesEnabled = false
                                isTiltGesturesEnabled = false
                            }
                            ready.cameraPosition = initialPosition()
                            ready.addOnCameraMoveListener { cameraTick.intValue++ }
                            ready.addOnCameraIdleListener { cameraTick.intValue++ }
                            map = ready
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
            LaunchedEffect(map, styleJson) { map?.setStyle(Style.Builder().fromJson(styleJson)) }
            LaunchedEffect(map, coveredPx) { map?.setPadding(0, 0, 0, coveredPx) }
            val camera = remember(map) { camera(map) }
            LaunchedEffect(map, scene.focus) { if (map != null) scene.focus.applyTo(camera) }

            map?.let { ready ->
                fun screen(point: PdGeoPoint): Offset {
                    cameraTick.intValue
                    val spot: PointF = ready.projection.toScreenLocation(LatLng(point.latitude, point.longitude))
                    return Offset(spot.x, spot.y)
                }
                scene.places.sortedBy { it.raised }.forEach { place ->
                    place.marker.Content(
                        Modifier
                            .pdOverlayAt { screen(place.position) }
                            .clickable { scene.onPlaceClick(place.id) },
                    )
                }
                scene.user.spots.forEach { spot -> PdUserDot.Content(Modifier.pdOverlayAt { screen(spot) }) }
            }

            PdPlainText(
                text = PdLiteralText(OpenFreeMapStyle.ATTRIBUTION),
                role = PdTextRole.LabelSmall,
                tone = PdContentTone.Secondary,
                maxLines = 1,
            ).Content(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = spacing.small, bottom = scene.coveredBottom + spacing.small)
                    .background(colors.surface.primary.copy(alpha = PdTheme.opacity.dim), PdTheme.shapes.small)
                    .padding(horizontal = spacing.tiny, vertical = spacing.xxSmall),
            )
        }
    }

    private fun initialPosition(): CameraPosition {
        val start = scene.user.spots.firstOrNull() ?: scene.places.firstOrNull()?.position
        return if (start == null) {
            CameraPosition.Builder().zoom(WORLD_ZOOM).build()
        } else {
            CameraPosition.Builder().target(LatLng(start.latitude, start.longitude)).zoom(CITY_ZOOM).build()
        }
    }

    private fun camera(map: MapLibreMap?): PdMapCamera = object : PdMapCamera {
        override fun moveTo(point: PdGeoPoint) {
            val target = LatLng(point.latitude, point.longitude)
            val zoom = max(map?.cameraPosition?.zoom ?: FOCUS_ZOOM, FOCUS_ZOOM)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(target, zoom))
        }
    }

    /** Forwards the lifecycle of the screen to the map view, which needs every callback. */
    @Composable
    private fun ManageLifecycle(mapView: MapView) {
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        DisposableEffect(lifecycle, mapView) {
            var destroyed = false
            fun destroy() {
                if (destroyed) return
                destroyed = true
                mapView.onPause()
                mapView.onStop()
                mapView.onDestroy()
            }
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> mapView.onStart()
                    Lifecycle.Event.ON_RESUME -> mapView.onResume()
                    Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                    Lifecycle.Event.ON_STOP -> mapView.onStop()
                    Lifecycle.Event.ON_DESTROY -> destroy()
                    else -> Unit
                }
            }
            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
                destroy()
            }
        }
    }
}
