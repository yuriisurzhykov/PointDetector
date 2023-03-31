package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponseMapper
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache

class CityResponseMapper(
    mapper: CityCacheToDomainMapper
) : UseCaseResponseMapper.AbstractList<CityCache, City>(mapper)