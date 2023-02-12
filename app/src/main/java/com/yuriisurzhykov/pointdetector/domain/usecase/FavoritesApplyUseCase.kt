package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.FavoritesRepository
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.favorites.FavoritesApply
import javax.inject.Inject

interface FavoritesApplyUseCase : FavoritesApply<PointUi> {

    abstract class Abstract constructor(
        private val mapper: Mapper<PointUi, PointCache>,
        private val repository: FavoritesRepository
    ) : FavoritesApplyUseCase {
        override fun applyFavorite(item: PointUi) {
            repository.remove(mapper.map(item))
        }
    }

    class Base @Inject constructor(mapper: Mapper<PointUi, PointCache>, repository: FavoritesRepository) :
        Abstract(mapper, repository)
}