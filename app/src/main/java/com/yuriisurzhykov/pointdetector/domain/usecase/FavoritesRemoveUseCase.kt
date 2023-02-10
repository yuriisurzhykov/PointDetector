package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.FavoritesRepository
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.favorites.FavoritesRemove
import javax.inject.Inject

interface FavoritesRemoveUseCase : FavoritesRemove<PointUi> {

    abstract class Abstract constructor(
        private val mapper: Mapper<PointUi, PointCache>,
        private val pointsRepository: FavoritesRepository
    ) : FavoritesRemoveUseCase {
        override fun remove(item: PointUi) {
            pointsRepository.remove(mapper.map(item))
        }
    }

    class Base @Inject constructor(mapper: Mapper<PointUi, PointCache>, repository: FavoritesRepository) :
        Abstract(mapper, repository)
}