package com.yuriisurzhykov.pointdetector.presentation.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse
import com.yuriisurzhykov.foodbanks.domain.city.City
import com.yuriisurzhykov.foodbanks.domain.city.CityListUseCase
import com.yuriisurzhykov.foodbanks.domain.city.CityNotDetectedException
import com.yuriisurzhykov.foodbanks.domain.city.SelectedCityUseCase
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** The selected city and the list the person chooses from. */
@HiltViewModel
class CityViewModel @Inject constructor(
    private val selectedCityUseCase: SelectedCityUseCase,
    private val cityListUseCase: CityListUseCase,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private val mutableSelected = MutableStateFlow<CityUi?>(null)
    val selected: StateFlow<CityUi?> = mutableSelected.asStateFlow()

    private val mutableList = MutableStateFlow<CityListState>(CityListState.Loading)
    val list: StateFlow<CityListState> = mutableList.asStateFlow()

    /** True after the automatic detection failed, until the person tries again or picks a city. */
    private val mutableDetectionFailed = MutableStateFlow(false)
    val detectionFailed: StateFlow<Boolean> = mutableDetectionFailed.asStateFlow()

    private var listJob: Job? = null

    init {
        refresh()
        loadCities()
    }

    /** Looks for the selected city again, for example when the location becomes available. */
    fun refresh() {
        dispatchers.launchBackground(viewModelScope) {
            mutableSelected.value = currentCity()
        }
    }

    fun select(city: CityUi) {
        mutableDetectionFailed.value = false
        mutableSelected.value = city
        dispatchers.launchBackground(viewModelScope) { selectedCityUseCase.select(city.code) }
    }

    /** Takes the city where the person is now. A failure is kept as a flag and the choice stays. */
    fun detect() {
        mutableDetectionFailed.value = false
        dispatchers.launchBackground(viewModelScope) {
            try {
                mutableSelected.value = selectedCityUseCase.detect().map(CityUi.Factory)
            } catch (e: CityNotDetectedException) {
                mutableDetectionFailed.value = true
            }
        }
    }

    fun retry() {
        mutableList.value = CityListState.Loading
        loadCities()
    }

    private suspend fun currentCity(): CityUi? {
        return try {
            selectedCityUseCase.selectedCity().map(CityUi.Factory)
        } catch (e: CityNotDetectedException) {
            null
        }
    }

    private fun loadCities() {
        listJob?.cancel()
        listJob = dispatchers.launchBackground(viewModelScope) {
            cityListUseCase.cities().collect { response ->
                mutableList.update { current -> CityListReducer.reduce(current, response.cities()) }
            }
        }
    }

    private fun UseCaseResponse<List<City>>.cities(): List<CityUi>? = when (this) {
        is UseCaseResponse.Success -> data
        is UseCaseResponse.NoNetwork -> data
        else -> null
    }?.map { it.map(CityUi.Factory) }
}
