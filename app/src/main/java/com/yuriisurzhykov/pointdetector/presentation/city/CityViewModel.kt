package com.yuriisurzhykov.pointdetector.presentation.city

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val selectedCityUseCase: SelectedCityUseCase,
    private val cityListUseCase: CityListUseCase,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private val selectedCity = MutableLiveData<CityUi?>()
    private val cities = MutableLiveData<List<CityUi>>(emptyList())

    init {
        refresh()
        loadCities()
    }

    fun observeSelectedCity(owner: LifecycleOwner, observer: Observer<CityUi?>) {
        selectedCity.observe(owner, observer)
    }

    fun selectedCityCode(): String? = selectedCity.value?.code

    fun availableCities(): List<CityUi> = cities.value.orEmpty()

    /**
     * Looks for the selected city again, for example when the location becomes available.
     */
    fun refresh() {
        dispatchers.launchBackground(viewModelScope) {
            selectedCity.postValue(currentCity())
        }
    }

    fun select(city: CityUi) {
        dispatchers.launchBackground(viewModelScope) {
            selectedCityUseCase.select(city.code)
            selectedCity.postValue(city)
        }
    }

    private suspend fun currentCity(): CityUi? {
        return try {
            selectedCityUseCase.selectedCity().map(CityUi.Factory)
        } catch (e: CityNotDetectedException) {
            null
        }
    }

    private fun loadCities() {
        dispatchers.launchBackground(viewModelScope) {
            cityListUseCase.cities().collect { response ->
                response.cities()?.let { list ->
                    cities.postValue(list.map { it.map(CityUi.Factory) })
                }
            }
        }
    }

    private fun UseCaseResponse<List<City>>.cities(): List<City>? = when (this) {
        is UseCaseResponse.Success -> data
        is UseCaseResponse.NoNetwork -> data
        else -> null
    }
}
