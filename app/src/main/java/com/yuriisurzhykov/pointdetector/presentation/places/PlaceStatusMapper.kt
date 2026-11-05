package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.hours.PlaceStanding
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdNextOpening
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdClosedUntil
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdClosingSoon
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdHoursUnknown
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdOpenNow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdOpensToday
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdStatusLine
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

/** Turns where a place stands into the status component and into the colour state of its pin. */
class PlaceStatusMapper @Inject constructor() {

    fun line(standing: PlaceStanding): PdStatusLine = when (standing) {
        is PlaceStanding.OpenUntil -> PdOpenNow(TimeLabels.resolver(standing.untilMinute))
        is PlaceStanding.ClosingSoon -> PdClosingSoon(standing.minutesLeft)
        is PlaceStanding.OpensToday -> PdOpensToday(TimeLabels.resolver(standing.atMinute))
        is PlaceStanding.ClosedUntil -> PdClosedUntil(PdNextOpening(dayText(standing), hoursText(standing)))
        is PlaceStanding.Unknown -> PdHoursUnknown
    }

    fun status(standing: PlaceStanding): PlaceStatus = when (standing) {
        is PlaceStanding.OpenUntil -> PlaceStatus.Open
        is PlaceStanding.ClosingSoon -> PlaceStatus.ClosingSoon
        is PlaceStanding.OpensToday, is PlaceStanding.ClosedUntil, is PlaceStanding.Unknown -> PlaceStatus.Closed
    }

    private fun dayText(standing: PlaceStanding.ClosedUntil): PdTextResolver =
        if (standing.daysAhead == TOMORROW) {
            PdStringText(R.string.place_next_tomorrow)
        } else {
            PdLiteralText(standing.day.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
        }

    private fun hoursText(standing: PlaceStanding.ClosedUntil): PdTextResolver =
        PdLiteralText("${TimeLabels.text(standing.startMinute)} \u2013 ${TimeLabels.text(standing.endMinute)}")

    private companion object {
        const val TOMORROW = 1
    }
}
