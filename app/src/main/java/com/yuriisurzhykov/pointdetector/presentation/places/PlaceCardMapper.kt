package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.toggles.PdHeartToggle
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlaceCard
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists.PdPlaceCardData
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import javax.inject.Inject

/** Makes the card of a place. All the data is turned into UI types here, never inside a component. */
class PlaceCardMapper @Inject constructor(
    private val statuses: PlaceStatusMapper,
    private val tags: PlaceTagsMapper,
) {

    fun key(point: PointUi): String = "place-${point.id}"

    fun card(
        row: PlaceRow,
        number: Int,
        selected: Boolean,
        onOpen: (PointUi) -> Unit,
        onFavorite: (PointUi) -> Unit,
    ): PdPlaceCard = PdPlaceCardData(
        key = key(row.point),
        number = number,
        name = PdLiteralText(row.point.placeName),
        address = PdLiteralText(row.point.address),
        distance = PdLiteralText(row.point.distanceString),
        status = statuses.line(row.standing),
        tags = tags.tags(row.point.help),
        favorite = PdHeartToggle(
            checked = row.favorite,
            description = PdStringText(if (row.favorite) R.string.places_favorite_remove else R.string.places_favorite_add),
            onCheckedChange = { onFavorite(row.point) },
        ),
        selected = selected,
        onOpen = { onOpen(row.point) },
    )
}
