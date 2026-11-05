package com.yuriisurzhykov.pointdetector.presentation.points.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesClock
import com.yuriisurzhykov.pointdetector.presentation.places.rememberPlacesClock
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdPinMapMarker
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdScrubTo
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdFocusOn
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdGeoPoint
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapFocus
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapPlace
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapScene
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdNoFocus
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdNoUser
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlinedIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.toggles.PdHeartToggle
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMapFactory
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdMapHero
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPlaceActionBar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPlacePage
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/**
 * The place screen: the map, the status with the day axis, the week, how to get help, and the
 * route and favorite buttons. Dragging the axis moves the "current time" of the screen, and the
 * status follows it; the real clock is kept so the person can see the place at any moment.
 */
@Composable
fun PlaceScreen(
    point: PointUi,
    actions: PlaceDetailsActions,
    mapFactory: PdMapFactory,
    mapper: PlaceDetailsMapper,
    clock: PlacesClock = rememberPlacesClock(),
) {
    var scrubMinute by rememberSaveable { mutableStateOf(NOT_SCRUBBED) }
    val shown = remember(clock, scrubMinute) {
        if (scrubMinute == NOT_SCRUBBED) clock else PlacesClock(clock.day, scrubMinute)
    }
    val scrub = remember { PdScrubTo { fraction -> scrubMinute = AxisTime.minuteOf(fraction) } }
    val sections = remember(point, shown, scrub) { mapper.sections(point, shown, scrub) }

    val status = remember(point, clock) { mapper.status(point, clock) }
    val hero = remember(point.id, status) {
        val position = geoPoint(point.coordinates.lat, point.coordinates.lng)
        val places = position?.let { listOf(PdMapPlace(point.id, it, PdPinMapMarker(status), raised = true)) }.orEmpty()
        val focus: PdMapFocus = position?.let { PdFocusOn(it) } ?: PdNoFocus
        PdMapHero(
            map = mapFactory.create(PdMapScene(places, PdNoUser, focus, 0.dp) {}),
            back = PdOutlinedIconButton(PdIcons.Back, PdStringText(R.string.place_back), actions::onBack),
            share = PdOutlinedIconButton(PdIcons.Share, PdStringText(R.string.place_share), actions::onShare),
        )
    }

    val favorite = point.favoriteState == FavoriteState.FavoriteEnabled
    val bar = remember(favorite, actions) {
        PdPlaceActionBar(
            primary = PdPrimaryButton(PdStringText(R.string.place_route), PdDecorativeIcon(PdIcons.Route), actions::onRoute),
            favorite = PdHeartToggle(
                checked = favorite,
                description = PdStringText(if (favorite) R.string.places_favorite_remove else R.string.places_favorite_add),
                onCheckedChange = { actions.onFavorite() },
            ),
        )
    }
    PdPlacePage(hero, sections, bar).Content(Modifier)
}

private const val NOT_SCRUBBED = -1

/** A point of the data, or nothing when the data holds coordinates that are not on the Earth. */
private fun geoPoint(latitude: Double, longitude: Double): PdGeoPoint? =
    runCatching { PdGeoPoint(latitude, longitude) }.getOrNull()
