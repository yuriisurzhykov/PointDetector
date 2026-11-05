package com.yuriisurzhykov.foodbanks.data.point

import com.yuriisurzhykov.foodbanks.data.point.cache.PointOrigin
import com.yuriisurzhykov.foodbanks.data.point.cache.PointWithHours
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsDao
import javax.inject.Inject

/**
 * A place the points of a city come from. Each source knows only its own points, and
 * [Combined] makes several of them look like a single one, which is all the UI sees.
 * A new kind of points is a new source added to the combination.
 */
interface PointsSource {

    suspend fun points(cityCode: String): List<PointWithHours>

    abstract class Abstract(
        private val pointsDao: PointsDao,
        private val origin: PointOrigin
    ) : PointsSource {

        override suspend fun points(cityCode: String) = pointsDao.pointsByCity(cityCode, origin)
    }

    /**
     * Points that were synced from the cloud.
     */
    class Cloud @Inject constructor(pointsDao: PointsDao) :
        Abstract(pointsDao, PointOrigin.CLOUD)

    /**
     * Points that were added by the user, the cloud knows nothing about them.
     */
    class User @Inject constructor(pointsDao: PointsDao) :
        Abstract(pointsDao, PointOrigin.USER)

    class Combined(private val sources: List<PointsSource>) : PointsSource {

        override suspend fun points(cityCode: String): List<PointWithHours> {
            return sources.flatMap { it.points(cityCode) }
        }
    }
}
