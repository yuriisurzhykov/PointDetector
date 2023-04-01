package com.yuriisurzhykov.foodbanks.domain.favorites

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponseMapper
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritePointCache
import javax.inject.Inject

class FavoritesUseCaseMapper @Inject constructor(
    mapper: FavoriteCacheMapper
) : UseCaseResponseMapper.AbstractList<FavoritePointCache, Favorite>(mapper)