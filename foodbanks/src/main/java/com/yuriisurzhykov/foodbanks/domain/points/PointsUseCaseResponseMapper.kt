package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponseMapper
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import javax.inject.Inject

class PointsUseCaseResponseMapper @Inject constructor(
    mapper: PointCacheToDomainMapper
) : UseCaseResponseMapper.AbstractList<PointCache, Point>(mapper)