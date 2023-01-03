package com.yuriisurzhykov.pointdetector.domain.usecase

import android.content.Context
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.data.cache.configs.SortingTypeConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FetchAvailableSortingTypesUseCase @Inject constructor(
    @ApplicationContext context: Context,
    measureUnitConfig: SortingTypeConfig
) : ConfigOptionsFetchUseCase.Abstract(context, measureUnitConfig, R.xml.sorting_options)