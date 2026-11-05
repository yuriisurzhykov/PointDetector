package com.yuriisurzhykov.foodbanks.data.point.user

import com.yuriisurzhykov.foodbanks.data.point.cache.PointOrigin
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsDao
import javax.inject.Inject

/**
 * The write side of the points added by the user. Reading them is done by
 * [com.yuriisurzhykov.foodbanks.data.point.PointsSource.User].
 */
interface UserPointsRepository {

    /**
     * A point with the same address in the same city is replaced.
     */
    suspend fun add(point: UserPoint)

    /**
     * Only the points added by the user can be removed, a cloud point is never touched.
     * @return true if the point was removed
     */
    suspend fun remove(pointId: Long): Boolean

    abstract class Abstract(
        private val pointsDao: PointsDao,
        private val mapper: UserPointToCacheMapper
    ) : UserPointsRepository {

        override suspend fun add(point: UserPoint) = pointsDao.insertOrUpdate(mapper.map(point))

        override suspend fun remove(pointId: Long) =
            pointsDao.deleteWithFavorites(pointId, PointOrigin.USER)
    }

    class Base @Inject constructor(
        pointsDao: PointsDao,
        mapper: UserPointToCacheMapper
    ) : Abstract(pointsDao, mapper)
}
