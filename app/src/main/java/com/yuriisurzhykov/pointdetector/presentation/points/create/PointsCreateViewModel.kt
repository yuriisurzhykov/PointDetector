package com.yuriisurzhykov.pointdetector.presentation.points.create

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.core.SingleLiveEvent
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SuggestedPlacesUseCase
import com.yuriisurzhykov.pointsdetector.uicomponents.WeekDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class PointsCreateViewModel @Inject constructor(
    private val geoSuggestedPlaces: SuggestedPlacesUseCase,
    private val savePlacesUseCase: SavePointUseCase,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private var timer: Timer? = null
    private val suggestedPoints = MutableLiveData<List<Point>>()
    private val creationState = MutableLiveData<PointCreateState>()
    private val selectedWorkingDays = MutableLiveData<List<WeekDay>>()

    fun observeSuggestedPlaces(owner: LifecycleOwner, observer: Observer<List<Point>>) {
        suggestedPoints.observe(owner, observer)
    }

    fun observeCreationState(owner: LifecycleOwner, observer: Observer<PointCreateState>) {
        creationState.observe(owner, observer)
    }

    fun updateEnteredPlaceName(name: String) {
        postTimerJob {
            val suggested = geoSuggestedPlaces.getSuggestedPlaces(name)
            suggestedPoints.postValue(suggested)
        }
    }

    fun savePointWithDays(days: List<WeekDay>) {
        dispatchers.launchBackground(viewModelScope) {
            selectedWorkingDays.postValue(days)
        }
    }

    fun selectPoint(point: Point) {
        dispatchers.launchBackground(viewModelScope) {
            creationState.postValue(PointCreateState.PointSelectedState(point))
        }
    }

    private fun postTimerJob(block: suspend () -> Unit) {
        timer?.cancel()
        timer = Timer()
        timer?.schedule(timerTask {
            dispatchers.launchBackground(viewModelScope) {
                block.invoke()
            }
        }, 400)
    }

}