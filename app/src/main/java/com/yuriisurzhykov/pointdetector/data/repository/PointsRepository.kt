package com.yuriisurzhykov.pointdetector.data.repository

import com.yuriisurzhykov.pointdetector.core.asFlow
import com.yuriisurzhykov.pointdetector.data.cache.PointsDao
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PointsRepository @Inject constructor(
    private val pointsDao: PointsDao
) : CrudRepository<PointCache> {

    override suspend fun delete(entity: PointCache) {
        pointsDao.delete(entity)
    }

    suspend fun deleteByAddressAndPlaceName(address: String, placeName: String) {
        pointsDao.deleteByAddressAndName(address, placeName)
    }

    override suspend fun fetch(): Flow<List<PointCache>> {
        return pointsDao.fetchAll().asFlow()
    }

    override suspend fun fetchByCondition(condition: String): Flow<List<PointCache>> {
        return flow { emit(pointsDao.fetchByPlaceContains(condition)) }
    }

    override suspend fun save(entity: PointCache) {
        pointsDao.insert(entity)
    }

    override suspend fun save(list: List<PointCache>) {
        list.forEach { pointCache -> pointsDao.insert(pointCache) }
    }
}