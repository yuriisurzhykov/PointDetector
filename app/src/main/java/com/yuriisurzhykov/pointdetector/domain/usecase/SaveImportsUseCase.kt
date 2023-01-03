package com.yuriisurzhykov.pointdetector.domain.usecase

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

interface SaveImportsUseCase<T> {

    suspend fun saveImports()

    class FirebaseImportPoints @Inject constructor(
        private val database: FirebaseDatabase,
        private val pointsFetchListener: PointsFetchValueListener
    ) : SaveImportsUseCase<List<Point>> {
        override suspend fun saveImports() {
            val pointsReference = database.getReference("points")
            pointsReference.addValueEventListener(pointsFetchListener)
        }
    }
}