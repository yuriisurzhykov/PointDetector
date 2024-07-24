package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponseMapper
import com.yuriisurzhykov.foodbanks.data.point.cache.PointWithHours
import javax.inject.Inject

interface PointsUseCaseResponseMapper : UseCaseResponseMapper.List<PointWithHours, Point> {

    class Base @Inject constructor(
        mapper: PointCacheToDomainMapper
    ) : PointsUseCaseResponseMapper, UseCaseResponseMapper.AbstractList<PointWithHours, Point>(mapper)
}