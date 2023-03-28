package com.yuriisurzhykov.foodbanks.data.city

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.core.map
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloud
import javax.inject.Inject

class CloudToCacheListMapper @Inject constructor(
    private val mapper: CloudToCacheMapper
) : Mapper.List<CityCloud, CityCache>() {
    override fun map(input: List<CityCloud>): List<CityCache> {
        return input.map { item -> item.map(mapper) }
    }
}