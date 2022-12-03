package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.location.Location
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.LocationManager
import com.yuriisurzhykov.pointdetector.domain.usecase.DeletePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAllPointsUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SearchPointUseCase
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.pointsdetector.uicomponents.list.EmptyStateData
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
    private val removePointUseCase: DeletePointUseCase,
    private val insertPointUserCase: SavePointUseCase
) : ViewModel() {

    private val isRunningAvailable = ObservableBoolean(true)
    private var searchCondition: String = emptyString()
    private val pointsList = MutableLiveData<List<ViewHolderItem>>()
    private val emptyStateDate = EmptyStateData()
    private var timer: Timer? = null

    fun updateUserLocation(location: Location) {
        LocationManager.setLocation(location)
        if (searchCondition.isEmpty() && isRunningAvailable.get()) {
            startLoadPoints(searchCondition)
        }
    }

    fun observePointsList(owner: LifecycleOwner, observer: Observer<List<ViewHolderItem>>) {
        pointsList.observe(owner, observer)
    }

    fun startLoadPoints(condition: String = "") {
        searchCondition = condition
        if (searchCondition.isNotEmpty() && isRunningAvailable.get()) {
            postTimerTask(300) {
                loadPlacesByCondition()
            }
        } else {
            loadAllPlaces()
        }
    }

    private suspend fun loadPlacesByCondition() {
        searchPointUseCase.searchPlaceByCondition(searchCondition)
            .onEach { postPointsList(it) }.collect()
    }

    private fun loadAllPlaces() {
        dispatchers.launchBackground(viewModelScope) {
            pointsListUseCase.fetchPoints()
                .onEach {
                    postPointsList(it)
                }.collect()
        }
    }

    private fun postPointsList(list: List<Point>) {
        val points = sortPointsList(list).ifEmpty { listOf(emptyStateDate) }
        pointsList.postValue(points)
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
        }
    }

    fun insertItem(point: Point) {
        dispatchers.launchBackground(viewModelScope) {
            insertPointUserCase.save(point)
        }
    }
}