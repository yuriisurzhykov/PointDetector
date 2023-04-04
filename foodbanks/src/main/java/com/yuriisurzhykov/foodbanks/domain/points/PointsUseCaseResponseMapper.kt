package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponseMapper
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import javax.inject.Inject

interface PointsUseCaseResponseMapper : UseCaseResponseMapper.List<PointCache, Point> {

    class Base @Inject constructor(
        mapper: PointCacheToDomainMapper
    ) : PointsUseCaseResponseMapper, UseCaseResponseMapper.AbstractList<PointCache, Point>(mapper)
}