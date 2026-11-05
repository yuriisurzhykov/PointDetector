package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.domain.hours.StandingResolver
import com.yuriisurzhykov.pointdetector.domain.hours.WorkingHoursParser
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi

/**
 * What the list and the map show: the rows of the selected tab and the counters of every tab.
 * Pure, so the rules of the tabs are tested without a screen.
 */
class PlacesModel(val rows: List<PlaceRow>, val counts: Map<PlacesTab, Int>) {

    companion object {
        fun build(
            points: List<PointUi>,
            tab: PlacesTab,
            clock: PlacesClock,
            resolver: StandingResolver = StandingResolver(),
        ): PlacesModel {
            val all = points.map { point ->
                val intervals = point.workingHours.mapNotNull {
                    WorkingHoursParser.parse(it.dayName, it.hoursFrom, it.hoursTo)
                }
                PlaceRow(point, resolver.resolve(intervals, clock.day, clock.minute))
            }
            val counts = mapOf(
                PlacesTab.All to all.size,
                PlacesTab.OpenNow to all.count { it.standing.isOpen },
                PlacesTab.Favorites to all.count { it.favorite },
            )
            val rows = when (tab) {
                PlacesTab.All -> all
                PlacesTab.OpenNow -> all.filter { it.standing.isOpen }
                PlacesTab.Favorites -> all.filter { it.favorite }
            }
            return PlacesModel(rows, counts)
        }
    }
}
