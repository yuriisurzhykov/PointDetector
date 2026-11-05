package com.yuriisurzhykov.pointdetector.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.presentation.city.CityViewModel
import com.yuriisurzhykov.pointdetector.presentation.map.LocationUpdates
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * The only screen of the app after the permission page: Compose from edge to edge ([AppRoot]). It
 * keeps what belongs to the platform, the position of the phone while the app is in front.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val places: PlacesViewModel by viewModels()
    private val cities: CityViewModel by viewModels()

    @Inject
    lateinit var ui: UiDependencies

    private val location = LocationUpdates(this) { position ->
        places.updateUserLocation(position)
        cities.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PdTheme { AppRoot(ui) }
        }
    }

    override fun onStart() {
        super.onStart()
        location.start()
    }

    override fun onStop() {
        super.onStop()
        location.stop()
    }
}
