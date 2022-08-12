package com.yuriisurzhykov.pointdetector.presentation.points.create

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.core.SingleLiveEvent
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.usecase.IGeoDecodeUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.ISavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.ISuggestedPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class PointsCreateViewModel @Inject constructor(
    private val geoSuggestedPlaces: ISuggestedPlacesUseCase,
    private val savePlacesUseCase: ISavePointUseCase,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private var timer: Timer? = null
    private val timerExecutor = Executors.newSingleThreadExecutor()
    private val suggestedPoints = MutableLiveData<List<Point>>()
    private val pointSaved = SingleLiveEvent<Boolean>()

    fun observeSuggestedPlaces(owner: LifecycleOwner, observer: Observer<List<Point>>) {
        suggestedPoints.observe(owner, observer)
    }

    fun observeSavedState(owner: LifecycleOwner, observer: Observer<Boolean>) {
        pointSaved.observe(owner, observer)
    }

    fun updateEnteredPlaceName(name: String) {
        postTimerJob {
            val suggested = geoSuggestedPlaces.getSuggestedPlaces(name)
            suggestedPoints.postValue(suggested)
        }
    }

    fun savePoint(point: Point) {
        dispatchers.launchBackground(viewModelScope) {
            savePlacesUseCase.save(point)
            pointSaved.postValue(true)
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