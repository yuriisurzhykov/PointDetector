package com.yuriisurzhykov.foodbanks.data.city

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.core.data.map
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloud
import javax.inject.Inject

class CloudToCacheListMapper @Inject constructor(
    private val mapper: CloudToCacheMapper
) : Mapper.AbstractList<CityCloud, CityCache>() {

    override fun map(input: CityCloud) = input.map(mapper)
}