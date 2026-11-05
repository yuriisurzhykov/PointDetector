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
        sync()
    }

    /**
     * Brings the list of cities up to date: what is cached is shown at once and the cloud answer
     * replaces it. The list that is already shown stays when the cloud cannot be reached.
     */
    fun sync() = loadCities()

    /** Looks for the selected city again, for example when the location becomes available. */
    fun refresh() {
        dispatchers.launchBackground(viewModelScope) {
            mutableSelected.value = currentCity()
        }
    }

    /**
     * The choice is saved first and only then announced: whoever reacts to the new city (the list
     * of points) reads it from the preference and must not find the old one there.
     */
    fun select(city: CityUi) {
        mutableDetectionFailed.value = false
        dispatchers.launchBackground(viewModelScope) {
            selectedCityUseCase.select(city.code)
            mutableSelected.value = city
        }
    }

    /** Takes the city where the person is now. A failure is kept as a flag and the choice stays. */
    fun detect() {
        mutableDetectionFailed.value = false
        dispatchers.launchBackground(viewModelScope) {
            try {
                mutableSelected.value = selectedCityUseCase.detect().map(CityUi.Factory)
                sync()
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
