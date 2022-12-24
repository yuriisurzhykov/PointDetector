package com.yuriisurzhykov.pointdetector.domain.usecase

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

interface SaveImportsUseCase<T> {

    suspend fun saveImports(fileName: String)

    abstract class Abstract constructor(
        private val savePointUseCase: SavePointUseCase
    ) : SaveImportsUseCase<List<Point>> {
        protected suspend fun savePointsToLocal(points: List<Point>) {
            points.forEach { point ->
                savePointUseCase.save(point)
            }
        }
    }

    class SavePoints @Inject constructor(
        private val importAssetsPointsUseCase: ImportAssetsPointsUseCase,
        savePointUseCase: SavePointUseCase
    ) : Abstract(savePointUseCase) {
        override suspend fun saveImports(fileName: String) {
            val points = importAssetsPointsUseCase.importPointsList(fileName)
            savePointsToLocal(points)
        }
    }

    class FirebaseImportPoints @Inject constructor(
        private val database: FirebaseDatabase,
        private val pointsFetchListener: PointsFetchValueListener
    ) : SaveImportsUseCase<List<Point>> {
        override suspend fun saveImports(fileName: String) {
            val pointsReference = database.getReference("points")
            pointsReference.addValueEventListener(pointsFetchListener)
        }
    }
}