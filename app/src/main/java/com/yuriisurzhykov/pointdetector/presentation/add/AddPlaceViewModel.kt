package com.yuriisurzhykov.pointdetector.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriisurzhykov.foodbanks.domain.city.CityNotDetectedException
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessage
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessages
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SuggestedPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_MILLIS = 300L

/** The three steps of adding a place: the search, the form with the hours, the result. */
@HiltViewModel
class AddPlaceViewModel @Inject constructor(
    private val suggestedPlaces: SuggestedPlacesUseCase,
    private val savePoint: SavePointUseCase,
    private val dispatchers: Dispatchers,
    private val messages: AppMessages,
) : ViewModel() {

    private val mutableState = MutableStateFlow(AddPlaceState())
    val state: StateFlow<AddPlaceState> = mutableState.asStateFlow()

    private var searchJob: Job? = null

    // ---- step 1: the search ----

    fun onQuery(text: String) {
        searchJob?.cancel()
        mutableState.update { it.copy(query = text, suggestions = if (text.isBlank()) Suggestions.Idle else Suggestions.Loading) }
        if (text.isBlank()) return
        searchJob = dispatchers.launchBackground(viewModelScope) {
            delay(SEARCH_DEBOUNCE_MILLIS)
            val found = suggestedPlaces.getSuggestedPlaces(text)
            mutableState.update {
                it.copy(suggestions = if (found.isEmpty()) Suggestions.NotFound else Suggestions.Found(found))
            }
        }
    }

    fun onPick(point: Point) = mutableState.update {
        it.copy(step = AddStep.Details, picked = point, name = point.placeName, address = point.address)
    }

    /** The place is not in the suggestions: the form starts with what was typed as the address. */
    fun onManual() = mutableState.update {
        it.copy(step = AddStep.Details, picked = null, name = "", address = it.query)
    }

    // ---- step 2: the form ----

    fun onName(text: String) = mutableState.update { it.copy(name = text) }

    fun onAddress(text: String) = mutableState.update { it.copy(address = text) }

    fun onAnswer(type: String, answer: FactAnswer) = mutableState.update { it.copy(answers = it.answers + (type to answer)) }

    fun onCitySheet(open: Boolean) = mutableState.update { it.copy(citySheet = open) }

    fun onOpenHours() = mutableState.update { it.copy(draft = HoursDraft()) }

    fun onCloseHours() = mutableState.update { it.copy(draft = null) }

    fun onDay(day: DayOfWeek) = updateDraft { it.toggled(day) }

    fun onFrom(digits: String) = updateDraft { it.copy(fromDigits = digits) }

    fun onTo(digits: String) = updateDraft { it.copy(toDigits = digits) }

    /** Adds the typed hours to the list; a day that already had hours takes the new ones. */
    fun onAddHours() = mutableState.update { current ->
        val group = current.draft?.toGroup() ?: return@update current
        current.copy(groups = HoursGroups.add(current.groups, group), draft = HoursDraft(), hoursError = false)
    }

    fun onRemoveHours(index: Int) = mutableState.update { it.copy(groups = HoursGroups.remove(it.groups, index)) }

    fun onSave() {
        val current = mutableState.value
        if (current.saving) return
        if (current.groups.isEmpty()) {
            mutableState.update { it.copy(hoursError = true) }
            return
        }
        mutableState.update { it.copy(saving = true, problem = null, hoursError = false) }
        dispatchers.launchBackground(viewModelScope) { save(current) }
    }

    // ---- step 3 and leaving ----

    /** Starts over: the same flow for one more place. */
    fun onAnother() {
        searchJob?.cancel()
        mutableState.value = AddPlaceState()
    }

    /** True when the back press was used inside the flow; false when the flow should be closed. */
    fun onBack(): Boolean {
        val current = mutableState.value
        return when {
            current.citySheet -> consume { it.copy(citySheet = false) }
            current.draft != null -> consume { it.copy(draft = null) }
            current.step == AddStep.Details && !current.saving -> consume { it.copy(step = AddStep.Search, problem = null, hoursError = false) }
            else -> false
        }
    }

    private suspend fun save(current: AddPlaceState) {
        val coordinates = coordinatesOf(current)
        if (coordinates == null) {
            fail(SaveProblem.AddressNotFound)
            return
        }
        val point = Point(
            address = current.address,
            coordinates = coordinates,
            placeName = current.name,
            workingHours = HoursGroups.toWeekDays(current.groups),
            help = FactAnswers.toHelp(current.answers),
        )
        try {
            savePoint.save(point)
            messages.post(AppMessage.PlaceAdded(current.name))
            mutableState.update {
                it.copy(
                    step = AddStep.Done,
                    saving = false,
                    saved = SavedPlace(current.name, current.address, current.groups, coordinates.lat, coordinates.lng),
                )
            }
        } catch (e: CityNotDetectedException) {
            fail(SaveProblem.CityNotSelected)
        } catch (e: Exception) {
            fail(SaveProblem.Failed)
        }
    }

    /** The coordinates of the picked suggestion, or, when the address was typed or changed, of what the geocoder finds. */
    private suspend fun coordinatesOf(current: AddPlaceState): LatLng? {
        val picked = current.picked
        if (picked != null && picked.address == current.address) return picked.coordinates
        return suggestedPlaces.getSuggestedPlaces(current.address).firstOrNull()?.coordinates
    }

    private fun fail(problem: SaveProblem) {
        messages.post(AppMessage.PlaceNotSaved(problem.toReason()))
        mutableState.update { it.copy(saving = false, problem = problem) }
    }

    private fun SaveProblem.toReason() = when (this) {
        SaveProblem.CityNotSelected -> AppMessage.PlaceNotSaved.Reason.CityNotSelected
        SaveProblem.AddressNotFound -> AppMessage.PlaceNotSaved.Reason.AddressNotFound
        SaveProblem.Failed -> AppMessage.PlaceNotSaved.Reason.Failed
    }

    private fun updateDraft(change: (HoursDraft) -> HoursDraft) =
        mutableState.update { current -> current.draft?.let { current.copy(draft = change(it)) } ?: current }

    private fun consume(change: (AddPlaceState) -> AddPlaceState): Boolean {
        mutableState.update(change)
        return true
    }
}
