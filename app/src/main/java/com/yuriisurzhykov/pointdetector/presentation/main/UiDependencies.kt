package com.yuriisurzhykov.pointdetector.presentation.main

import com.yuriisurzhykov.pointdetector.domain.messages.AppMessages
import com.yuriisurzhykov.pointdetector.presentation.add.AddPlaceMapper
import com.yuriisurzhykov.pointdetector.presentation.messages.ToastMessages
import com.yuriisurzhykov.pointdetector.presentation.places.PlaceCardMapper
import com.yuriisurzhykov.pointdetector.presentation.places.PlaceStatusMapper
import com.yuriisurzhykov.pointdetector.presentation.points.details.PlaceDetailsMapper
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMapFactory
import javax.inject.Inject

/** What the screens need to turn their state into components: the map and the mappers, injected once. */
class UiDependencies @Inject constructor(
    val mapFactory: PdMapFactory,
    val cards: PlaceCardMapper,
    val statuses: PlaceStatusMapper,
    val addMapper: AddPlaceMapper,
    val details: PlaceDetailsMapper,
    val messages: AppMessages,
    val toasts: ToastMessages,
)
