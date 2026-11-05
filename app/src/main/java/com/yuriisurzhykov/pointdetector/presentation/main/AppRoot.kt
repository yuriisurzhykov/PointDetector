package com.yuriisurzhykov.pointdetector.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesRoute
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesViewModel
import com.yuriisurzhykov.pointdetector.presentation.points.details.PlaceRoute

/**
 * The whole app is two screens: the main one with the map, and a place that rises over it. The
 * main screen stays composed under the place, so the map and the sheet are where they were left.
 */
@Composable
fun AppRoot(ui: UiDependencies, places: PlacesViewModel = hiltViewModel()) {
    var opened by rememberSaveable { mutableStateOf<PointUi?>(null) }
    // The place keeps being drawn while it slides away, after [opened] is already null.
    var lastOpened by remember { mutableStateOf<PointUi?>(null) }
    SideEffect { opened?.let { lastOpened = it } }

    val close = {
        opened = null
        // The heart of the place may have been changed there.
        places.reload()
    }

    Box(Modifier.fillMaxSize()) {
        PlacesRoute(ui, onOpenPlace = { opened = it })
        AnimatedVisibility(
            visible = opened != null,
            enter = slideInVertically(PdTheme.motion.spatial()) { it },
            exit = slideOutVertically(PdTheme.motion.spatial()) { it },
        ) {
            (opened ?: lastOpened)?.let { PlaceRoute(it, ui, onBack = close) }
        }
    }
    BackHandler(enabled = opened != null) { close() }
}
