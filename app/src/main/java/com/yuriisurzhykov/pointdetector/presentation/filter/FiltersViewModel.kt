package com.yuriisurzhykov.pointdetector.presentation.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.data.cache.configs.MeasureUnitConfig
import com.yuriisurzhykov.pointdetector.data.cache.configs.SortingTypeConfig
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAvailableMeasureUnitUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAvailableSortingTypesUseCase
import com.yuriisurzhykov.pointdetector.domain.entities.SelectOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** The sorting and the units of the filters sheet. Nothing is saved until [apply]. */
@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val sortingTypes: FetchAvailableSortingTypesUseCase,
    private val measureUnits: FetchAvailableMeasureUnitUseCase,
    private val measureUnitConfig: MeasureUnitConfig,
    private val sortingTypeConfig: SortingTypeConfig,
    private val dispatchers: Dispatchers,
) : ViewModel() {

    private val mutableState = MutableStateFlow(FiltersState())
    val state: StateFlow<FiltersState> = mutableState.asStateFlow()

    init {
        load()
    }

    /** Reads the options and the saved choice again, e.g. when the sheet is opened. */
    fun load() {
        mutableState.update { it.copy(applied = false) }
        dispatchers.launchBackground(viewModelScope) {
            val sorts = sortingTypes.fetch()
            val units = measureUnits.fetch()
            mutableState.value = FiltersState(
                sorts = sorts.map(::option),
                units = units.map(::option),
                sortId = sorts.firstOrNull { it.isSelected }?.selectId ?: FilterDefaults.SORT,
                unitId = units.firstOrNull { it.isSelected }?.selectId ?: FilterDefaults.unit(),
            )
        }
    }

    fun selectSort(id: String) = mutableState.update { it.copy(sortId = id) }

    fun selectUnit(id: String) = mutableState.update { it.copy(unitId = id) }

    fun reset() = mutableState.update { it.copy(sortId = FilterDefaults.SORT, unitId = FilterDefaults.unit()) }

    fun apply() {
        val chosen = mutableState.value
        dispatchers.launchBackground(viewModelScope) {
            sortingTypeConfig.createConfig(chosen.sortId)
            measureUnitConfig.createConfig(chosen.unitId)
            mutableState.update { it.copy(applied = true) }
        }
    }

    private fun option(source: SelectOption) = FilterOption(source.selectId, source.selectLabel)
}
