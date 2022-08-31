package com.yuriisurzhykov.pointdetector.presentation.main

import androidx.lifecycle.*
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAllPointsUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SaveImportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val importDataUseCase: SaveImportsUseCase<List<Point>>,
    private val pointsListUseCase: FetchAllPointsUseCase,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private val isImportOptionEnabled = MutableLiveData(true)

    init {
        dispatchers.launchBackground(viewModelScope) {
            pointsListUseCase.fetchPoints().onEach { isImportOptionEnabled.postValue(it.isEmpty()) }.collect()
        }
    }

    fun observeImportOptionVisibility(owner: LifecycleOwner, observer: Observer<Boolean>) {
        isImportOptionEnabled.observe(owner, observer)
    }

    fun importData() {
        dispatchers.launchBackground(viewModelScope) {
            importDataUseCase.saveImports("sample1.json")
        }
    }
}