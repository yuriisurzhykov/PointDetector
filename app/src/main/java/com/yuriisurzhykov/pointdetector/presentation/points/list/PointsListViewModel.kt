package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.LocationManager
import com.yuriisurzhykov.pointdetector.domain.usecase.IFetchAllPointsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PointsListViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val pointsListUseCase: IFetchAllPointsUseCase
) : ViewModel() {

    private val pointsList = MutableLiveData<List<Point>>()

    fun updateUserLocation(location: Location) {
        LocationManager.setLocation(location)
        startLoadPoints()
    }

    fun observePointsList(owner: LifecycleOwner, observer: Observer<List<Point>>) {
        pointsList.observe(owner, observer)
    }

    fun startLoadPoints() {
        dispatchers.launchBackground(viewModelScope) {
            pointsListUseCase.fetchPoints()
                .onEach { pointsList.postValue(it.sortedBy { point -> point.distance }) }.collect()
        }
    }
}