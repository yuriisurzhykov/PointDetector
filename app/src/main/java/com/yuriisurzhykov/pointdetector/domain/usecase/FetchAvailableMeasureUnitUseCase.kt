package com.yuriisurzhykov.pointdetector.domain.usecase

import android.content.Context
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.data.cache.configs.MeasureUnitConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FetchAvailableMeasureUnitUseCase @Inject constructor(
    @ApplicationContext context: Context,
    measureUnitConfig: MeasureUnitConfig,
) : ConfigOptionsFetchUseCase.Abstract(context, measureUnitConfig, R.xml.measure_units)