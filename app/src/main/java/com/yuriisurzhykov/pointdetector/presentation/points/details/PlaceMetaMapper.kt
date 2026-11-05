package com.yuriisurzhykov.pointdetector.presentation.points.details

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import javax.inject.Inject
import kotlin.math.roundToInt

private const val METERS_IN_KM = 1000.0
private const val WALK_MINUTES_PER_KM = 12.5
private const val DRIVE_MINUTES_PER_KM = 3.2
private const val MINUTES_IN_HOUR = 60

/** "0.5 km · 6 min on foot · 2 min by car" for the title block of the place. */
class PlaceMetaMapper @Inject constructor() {

    fun meta(point: PointUi): PdTextResolver {
        if (point.distanceDouble <= 0.0) return PdLiteralText(point.distanceString)
        return PdStringText(
            R.string.place_meta,
            listOf(
                PdLiteralText(point.distanceString),
                duration(walkMinutes(point.distanceDouble)),
                duration(driveMinutes(point.distanceDouble)),
            ),
        )
    }

    /** [meters] is what the point holds: the distance in meters, whatever unit is shown to the person. */
    fun walkMinutes(meters: Double): Int = minutes(meters, WALK_MINUTES_PER_KM)

    fun driveMinutes(meters: Double): Int = minutes(meters, DRIVE_MINUTES_PER_KM)

    private fun minutes(meters: Double, perKm: Double): Int =
        (meters / METERS_IN_KM * perKm).roundToInt().coerceAtLeast(1)

    private fun duration(minutes: Int): PdTextResolver {
        val hours = minutes / MINUTES_IN_HOUR
        val rest = minutes % MINUTES_IN_HOUR
        return when {
            hours == 0 -> PdStringText(R.string.place_minutes, listOf(PdLiteralText(rest.toString())))
            rest == 0 -> PdStringText(R.string.place_hours_only, listOf(PdLiteralText(hours.toString())))
            else -> PdStringText(
                R.string.place_hours_minutes,
                listOf(PdLiteralText(hours.toString()), PdLiteralText(rest.toString())),
            )
        }
    }
}
