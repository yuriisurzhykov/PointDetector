package com.yuriisurzhykov.foodbanks.domain.favorites

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritePointCache
import javax.inject.Inject

interface FavoriteCacheMapper : Mapper.List<FavoritePointCache, Favorite> {

    class Base @Inject constructor() : FavoriteCacheMapper,
        Mapper.AbstractList<FavoritePointCache, Favorite>() {
        override fun mapSingle(input: FavoritePointCache) = Favorite.Base(
            input.favoriteId,
            input.pointId,
            input.pointName,
            input.pointAddress
        )
    }
}
