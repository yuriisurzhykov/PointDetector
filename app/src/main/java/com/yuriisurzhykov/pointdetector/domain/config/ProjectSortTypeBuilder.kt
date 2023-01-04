package com.yuriisurzhykov.pointdetector.domain.config

import com.yuriisurzhykov.pointdetector.domain.usecase.ConfigUnifiedSource
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import javax.inject.Inject

class ProjectSortTypeBuilder @Inject constructor(
    private val configUnifiedSource: ConfigUnifiedSource
) : SortingTypeExecutor<PointUi> {

    private val pointsSorterByName = PointsSortByNameExecutor()
    private val pointsSorterByDistance = PointsSortByDistanceExecutor()

    override suspend fun performSort(items: Collection<PointUi>): Collection<PointUi> {
        return if (configUnifiedSource.sortTypeConfigValue() == "sort_by_name") {
            pointsSorterByName.performSort(items)
        } else {
            pointsSorterByDistance.performSort(items)
        }
    }
}