package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.location.Location
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.LocationManager
import com.yuriisurzhykov.pointdetector.domain.usecase.DeletePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAllPointsUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SearchPointUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

fun emptyString(): String {
    return ""
}

@HiltViewModel
class PointsListViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val pointsListUseCase: FetchAllPointsUseCase,
    private val searchPointUseCase: SearchPointUseCase,
    private val removePointUseCase: DeletePointUseCase
) : ViewModel() {

    private var searchCondition: String = emptyString()

    private val pointsList = MutableLiveData<List<Point>>()
    private var timer: Timer? = null

    fun updateUserLocation(location: Location) {
        LocationManager.setLocation(location)
        if (searchCondition.isEmpty()) {
            startLoadPoints(searchCondition)
        }
    }

    fun observePointsList(owner: LifecycleOwner, observer: Observer<List<Point>>) {
        pointsList.observe(owner, observer)
    }

    fun startLoadPoints(condition: String = "") {
        searchCondition = condition
        if (searchCondition.isNotEmpty()) {
            postTimerTask(300) {
                loadPlacesByCondition()
            }
        } else {
            loadAllPlaces()
        }
    }

    private suspend fun loadPlacesByCondition() {
        searchPointUseCase.searchPlaceByCondition(searchCondition)
            .onEach { pointsList.postValue(sortPointsList(it)) }.collect()
    }

    private fun loadAllPlaces() {
        dispatchers.launchBackground(viewModelScope) {
            pointsListUseCase.fetchPoints()
                .onEach { pointsList.postValue(sortPointsList(it)) }.collect()
        }
    }

    private fun sortPointsList(list: List<Point>): List<Point> {
        return list.sortedBy { point -> point.distance }
    }

    private fun postTimerTask(scheduleTime: Long, block: suspend () -> Unit) {
        timer?.cancel()
        timer = Timer()
        timer?.schedule(
            timerTask { dispatchers.launchBackground(viewModelScope) { block.invoke() } },
            scheduleTime
        )
    }

    fun getSearchCondition(): CharSequence {
        return searchCondition
    }

    fun removeItem(item: Point) {
        dispatchers.launchBackground(viewModelScope) {
            removePointUseCase.delete(item)
            startLoadPoints(searchCondition)
        }
    }
}