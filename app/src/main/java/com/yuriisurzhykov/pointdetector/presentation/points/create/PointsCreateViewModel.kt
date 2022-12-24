package com.yuriisurzhykov.pointdetector.presentation.points.create

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SuggestedPlacesUseCase
import com.yuriisurzhykov.pointdetector.presentation.list.StartSearchData
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.pointsdetector.uicomponents.workday.entity.WeekDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class PointsCreateViewModel @Inject constructor(
    private val geoSuggestedPlacesUseCase: SuggestedPlacesUseCase,
    private val savePlacesUseCase: SavePointUseCase,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private var timer: Timer? = null
    private val suggestedPoints = MutableLiveData<List<ViewHolderItem>>(listOf(StartSearchData()))
    private val creationState = MutableLiveData<PointCreateState>()
    private val selectedWorkingDays = MutableLiveData<List<WeekDay>>()
    private val enteredPointName = ObservableField<String>()
    private val errorMessage = MutableLiveData<Int>()
    private val loading = ObservableBoolean()

    fun observeSuggestedPlaces(owner: LifecycleOwner, observer: Observer<List<ViewHolderItem>>) {
        suggestedPoints.observe(owner, observer)
    }

    fun observeErrorMessage(owner: LifecycleOwner, observer: Observer<Int>) {
        errorMessage.observe(owner, observer)
    }

    fun observeCreationState(owner: LifecycleOwner, observer: Observer<PointCreateState>) {
        creationState.observe(owner, observer)
    }

    fun updateEnteredPlaceName(name: String) {
        loading.set(true)
        postTimerJob {
            val suggested = geoSuggestedPlacesUseCase.getSuggestedPlaces(name)
            postData(suggested, name)
            dispatchers.changeToUi { loading.set(false) }
        }
    }

    fun updateSelectedPlaceName(name: String) {
        enteredPointName.set(name)
    }

    fun savePointWithDays(days: List<WeekDay>) {
        dispatchers.launchBackground(viewModelScope) {
            dispatchers.changeToUi { loading.set(true) }
            if (days.isNotEmpty()) {
                if (days.all { it.isCorrect() }) {
                    selectedWorkingDays.postValue(days)
                    saveSelectedPlace(enteredPointName.get().orEmpty(), days)
                    creationState.postValue(PointCreateState.SavedPointState)
                } else {
                    postError(R.string.error_incorrect_selected_days)
                }
            } else {
                postError(R.string.error_days_not_selected)
            }
            dispatchers.changeToUi { loading.set(false) }
        }
    }

    private suspend fun saveSelectedPlace(name: String, days: List<WeekDay>) {
        val state = creationState.value as PointCreateState.PointSelectedState
        val point = Point(
            state.point.address,
            state.point.coordinates,
            state.point.distance,
            name,
            days,
            false
        )
        savePlacesUseCase.save(point)
    }

    fun selectPoint(point: Point) {
        dispatchers.launchBackground(viewModelScope) {
            creationState.postValue(PointCreateState.PointSelectedState(point))
        }
    }

    private fun postError(errorRes: Int) {
        errorMessage.postValue(errorRes)
    }

    private fun postData(list: List<Point>, enteredPointName: String) {
        val emptyList = if (enteredPointName.isEmpty()) {
            listOf(StartSearchData())
        } else listOf(EmptySearchData())
        suggestedPoints.postValue(list.ifEmpty { emptyList })
    }

    private fun postTimerJob(block: suspend () -> Unit) {
        timer?.cancel()
        timer = Timer()
        timer?.schedule(timerTask {
            dispatchers.launchBackground(viewModelScope) {
                block.invoke()
            }
        }, 300)
    }

}