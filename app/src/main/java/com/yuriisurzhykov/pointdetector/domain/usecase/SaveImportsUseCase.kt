package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

interface SaveImportsUseCase<T> {

    suspend fun saveImports(fileName: String)

    class SavePoints @Inject constructor(
        private val importAssetsPointsUseCase: ImportAssetsPointsUseCase,
        private val savePointUseCase: SavePointUseCase
    ) : SaveImportsUseCase<List<Point>> {
        override suspend fun saveImports(fileName: String) {
            importAssetsPointsUseCase.importPointsList(fileName).forEach {
                savePointUseCase.save(it)
            }
        }
    }

}