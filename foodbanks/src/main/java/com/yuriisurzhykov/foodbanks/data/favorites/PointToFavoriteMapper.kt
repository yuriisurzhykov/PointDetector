package com.yuriisurzhykov.foodbanks.data.favorites

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache

interface PointToFavoriteMapper : Mapper<PointCache, FavoritePointCache> {

    class Base : Mapper.Abstract<PointCache, FavoritePointCache>() {
        override fun map(input: PointCache) =
            FavoritePointCache(
                0,
                input.pointId,
                input.placeName,
                input.address
            )
    }
}