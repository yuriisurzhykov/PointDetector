package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.location.Location
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.config.ProjectSortTypeBuilder
import com.yuriisurzhykov.pointdetector.domain.mappers.PointUiToCacheMapper
import com.yuriisurzhykov.pointdetector.domain.services.LocationManager
import com.yuriisurzhykov.pointdetector.domain.usecase.DeletePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAllPointsUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SearchPointUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointsdetector.uicomponents.list.EmptyStateData
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
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
    private val insertPointUserCase: SavePointUseCase,
    private val pointsSorter: ProjectSortTypeBuilder,
    private val uiPointMapper: PointUiToCacheMapper
) : ViewModel() {

    private var searchCondition: String = emptyString()
    private val pointsList = MutableLiveData<List<ViewHolderItem>>()
    private val emptyStateDate = EmptyStateData()
    private var timer: Timer? = null

    fun updateUserLocation(location: Location) {
        LocationManager.setLocation(location)
        if (searchCondition.isEmpty()) {
            startLoadPoints(searchCondition)
        }
    }

    fun observePointsList(owner: LifecycleOwner, observer: Observer<List<ViewHolderItem>>) {
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

    private suspend fun postPointsList(list: List<PointUi>) {
        val points = sortPointsList(list).ifEmpty { listOf(emptyStateDate) }
        pointsList.postValue(points)
    }

    private suspend fun sortPointsList(list: List<PointUi>): List<PointUi> {
        return ArrayList(pointsSorter.performSort(list))
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

    fun removeItem(item: PointUi) {
        dispatchers.launchBackground(viewModelScope) {
            removePointUseCase.delete(uiPointMapper.map(item))
        }
    }

    fun insertItem(point: PointUi) {
        dispatchers.launchBackground(viewModelScope) {
            insertPointUserCase.save(uiPointMapper.map(point))
        }
    }
}