package com.yuriisurzhykov.pointdetector.domain.usecase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.mappers.PointToCacheMapper
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

interface PointsFetchValueListener : ValueEventListener

class PointsFirebaseListFetchListener @Inject constructor(
    private val dataSnapshotToListMapper: Mapper<DataSnapshot, List<Point>>,
    private val dispatchers: Dispatchers,
    private val pointMapper: PointToCacheMapper,
    private val savePointsUseCase: SavePointUseCase
) : PointsFetchValueListener {

    private val TAG = PointsFirebaseListFetchListener::class.simpleName

    override fun onDataChange(snapshot: DataSnapshot) {
        val mapped = dataSnapshotToListMapper.map(snapshot)
        dispatchers.launchBackground(CoroutineScope(kotlinx.coroutines.Dispatchers.IO)) {
            mapped.forEach { point ->
                savePointsUseCase.save(pointMapper.map(point))
            }
        }
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d(TAG, "onCancelled: ${error.message}")
    }
}