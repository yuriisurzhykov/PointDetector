package com.yuriisurzhykov.foodbanks.domain.favorites

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritePointCache

interface FavoriteCacheMapper : Mapper.List<FavoritePointCache, Favorite> {

    class Base : Mapper.AbstractList<FavoritePointCache, Favorite>() {
        override fun map(input: FavoritePointCache) = Favorite.Base(
            input.favoriteId,
            input.pointId,
            input.pointName,
            input.pointAddress
        )
    }
}