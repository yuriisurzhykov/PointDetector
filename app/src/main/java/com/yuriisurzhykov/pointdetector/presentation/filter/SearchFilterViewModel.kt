package com.yuriisurzhykov.pointdetector.presentation.filter

import androidx.lifecycle.*
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.data.cache.configs.MeasureUnitConfig
import com.yuriisurzhykov.pointdetector.data.cache.configs.SortingTypeConfig
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAvailableMeasureUnitUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAvailableSortingTypesUseCase
import com.yuriisurzhykov.pointdetector.uicomponents.textselect.SelectOption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchFilterViewModel @Inject constructor(
    private val fetchAvailableSortingTypesUseCase: FetchAvailableSortingTypesUseCase,
    private val fetchAvailableMeasureUnitsUseCase: FetchAvailableMeasureUnitUseCase,
    private val measureUnitConfig: MeasureUnitConfig,
    private val sortingTypeConfig: SortingTypeConfig,
    private val dispatchers: Dispatchers,
) : ViewModel() {

    private val sortingOptions = MutableLiveData<List<SelectOption>>()
    private val measureTypeOptions = MutableLiveData<List<SelectOption>>()
    private val isFinishedUpdatingConfigs = MutableLiveData(false)

    init {
        loadUiData()
    }

    fun observeMeasureTypes(owner: LifecycleOwner, observer: Observer<List<SelectOption>>) {
        measureTypeOptions.observe(owner, observer)
    }

    fun observeSortingTypes(owner: LifecycleOwner, observer: Observer<List<SelectOption>>) {
        sortingOptions.observe(owner, observer)
    }

    fun observeConfigsChanged(owner: LifecycleOwner, observer: Observer<Boolean>) {
        isFinishedUpdatingConfigs.observe(owner, observer)
    }

    fun loadUiData() {
        dispatchers.launchBackground(viewModelScope) {
            sortingOptions.postValue(fetchAvailableSortingTypesUseCase.fetch())
            measureTypeOptions.postValue(fetchAvailableMeasureUnitsUseCase.fetch())
        }
    }

    fun updateConfigs(measureUnit: SelectOption, sortType: SelectOption) {
        dispatchers.launchBackground(viewModelScope) {
            changeSortType(sortType)
            changeMeasureType(measureUnit)
            isFinishedUpdatingConfigs.postValue(true)
        }
    }

    private suspend fun changeSortType(item: SelectOption) {
        sortingTypeConfig.createConfig(item.selectId)
    }

    private suspend fun changeMeasureType(item: SelectOption) {
        measureUnitConfig.createConfig(item.selectId)
    }

}