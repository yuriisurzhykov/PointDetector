package com.yuriisurzhykov.pointdetector.presentation.add

import com.yuriisurzhykov.pointdetector.domain.entities.Point
import java.time.DayOfWeek

/** What the person can do while adding a place. The screens only report; the host decides. */
interface AddPlaceActions {
    fun onBack()
    fun onQuery(text: String)
    fun onPick(point: Point)
    fun onManual()
    fun onName(text: String)
    fun onAddress(text: String)
    fun onAnswer(type: String, answer: FactAnswer)
    fun onCity()
    fun onOpenHours()
    fun onCloseHours()
    fun onDay(day: DayOfWeek)
    fun onFrom(digits: String)
    fun onTo(digits: String)
    fun onAddHours()
    fun onRemoveHours(index: Int)
    fun onSave()
    fun onShowOnMap()
    fun onAnother()
}
