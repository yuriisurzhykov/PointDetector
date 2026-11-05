package com.yuriisurzhykov.foodbanks.points

import com.yuriisurzhykov.foodbanks.data.point.CloudToCachePointMapper
import com.yuriisurzhykov.foodbanks.data.point.PointsSource
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import com.yuriisurzhykov.foodbanks.data.point.cache.PointOrigin
import com.yuriisurzhykov.foodbanks.data.point.cache.PointWithHours
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsDao
import com.yuriisurzhykov.foodbanks.data.point.cache.WorkingHourCache
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import com.yuriisurzhykov.foodbanks.data.point.user.UserPointToCacheMapper
import com.yuriisurzhykov.foodbanks.data.point.user.UserPointsRepository
import com.yuriisurzhykov.foodbanks.data.prefs.NoPreferenceException
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import com.yuriisurzhykov.foodbanks.domain.points.UserPointsUseCase
import com.yuriisurzhykov.geo.LatLng
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val City = "sacramento"
private const val OtherCity = "oakland"
private const val Address = "5901 San Juan Ave"

class UserPointsTest {

    private lateinit var dao: FakePointsDao
    private lateinit var preference: FakePreference
    private lateinit var combined: PointsSource
    private lateinit var useCase: UserPointsUseCase
    private lateinit var userRepository: UserPointsRepository

    private val cloudMapper = CloudToCachePointMapper.Base()

    @Before
    fun `init resources for test`() {
        dao = FakePointsDao()
        preference = FakePreference(City)
        combined = PointsSource.Combined(listOf(PointsSource.Cloud(dao), PointsSource.User(dao)))
        userRepository = UserPointsRepository.Base(dao, UserPointToCacheMapper.Base())
        useCase = UserPointsUseCase.Base(preference, userRepository)
    }

    @Test
    fun `test user point is shown together with cloud points of the same city`(): Unit = runBlocking {
        syncCloud(cloudPoint("Cloud place", "Cloud address"))
        useCase.add("My place", "My address", LatLng(1.0, 2.0), emptyList())

        val points = combined.points(City)

        assertEquals(listOf("Cloud place", "My place"), points.map { it.point.placeName })
        assertEquals(listOf(PointOrigin.CLOUD, PointOrigin.USER), points.map { it.point.origin })
    }

    @Test
    fun `test user point is shown only in its own city`(): Unit = runBlocking {
        useCase.add("My place", "My address", LatLng(1.0, 2.0), emptyList())

        assertTrue(combined.points(OtherCity).isEmpty())
    }

    @Test
    fun `test each source sees only its own points`(): Unit = runBlocking {
        syncCloud(cloudPoint("Cloud place", "Cloud address"))
        useCase.add("My place", "My address", LatLng(1.0, 2.0), emptyList())

        assertEquals(listOf("Cloud place"), PointsSource.Cloud(dao).points(City).map { it.point.placeName })
        assertEquals(listOf("My place"), PointsSource.User(dao).points(City).map { it.point.placeName })
    }

    @Test
    fun `test cloud sync does not touch user point with the same address`(): Unit = runBlocking {
        useCase.add("My place", Address, LatLng(1.0, 2.0), emptyList())

        syncCloud(cloudPoint("Cloud place", Address))
        syncCloud(cloudPoint("Cloud place renamed", Address))

        val points = combined.points(City)
        assertEquals(listOf("Cloud place renamed", "My place"), points.map { it.point.placeName })
    }

    @Test
    fun `test repeated cloud sync updates the point instead of duplicating it`(): Unit = runBlocking {
        syncCloud(cloudPoint("Cloud place", Address, hours("09:00")))
        val id = combined.points(City).single().point.pointId

        syncCloud(cloudPoint("Cloud place", Address, hours("10:00")))

        val point = combined.points(City).single()
        assertEquals(id, point.point.pointId)
        assertEquals(listOf("10:00"), point.workingHours.map { it.hoursFrom })
    }

    @Test
    fun `test adding user point with the same address replaces it`(): Unit = runBlocking {
        useCase.add("My place", Address, LatLng(1.0, 2.0), emptyList())
        useCase.add("My place renamed", Address, LatLng(1.0, 2.0), emptyList())

        assertEquals(listOf("My place renamed"), combined.points(City).map { it.point.placeName })
    }

    @Test
    fun `test user point keeps its working hours`(): Unit = runBlocking {
        val hours = listOf(WorkingHour("Mon", -1, "09:00", "12:00"))
        useCase.add("My place", Address, LatLng(1.0, 2.0), hours)

        val point = combined.points(City).single()
        assertEquals(LatLng(1.0, 2.0), point.point.coordinates)
        assertEquals(listOf("Mon"), point.workingHours.map { it.dayName })
    }

    @Test
    fun `test user point is removed together with its favorites`(): Unit = runBlocking {
        useCase.add("My place", Address, LatLng(1.0, 2.0), emptyList())
        val id = combined.points(City).single().point.pointId

        assertTrue(useCase.remove(id))

        assertTrue(combined.points(City).isEmpty())
        assertEquals(listOf(id), dao.favoritesRemovedFor)
    }

    @Test
    fun `test cloud point cannot be removed as user point`(): Unit = runBlocking {
        syncCloud(cloudPoint("Cloud place", Address))
        val id = combined.points(City).single().point.pointId

        assertFalse(useCase.remove(id))

        assertEquals(1, combined.points(City).size)
        assertTrue(dao.favoritesRemovedFor.isEmpty())
    }

    @Test
    fun `test removing unknown point does nothing`(): Unit = runBlocking {
        assertFalse(useCase.remove(42))
    }

    @Test
    fun `test user point cannot be added when there is no selected city`(): Unit = runBlocking {
        preference.value = null

        val failure = runCatching {
            useCase.add("My place", Address, LatLng(1.0, 2.0), emptyList())
        }.exceptionOrNull()

        assertTrue(failure is NoPreferenceException)
    }

    private suspend fun syncCloud(vararg points: PointCloud) {
        cloudMapper.map(points.toList()).forEach { dao.insertOrUpdate(it) }
    }

    private fun cloudPoint(name: String, address: String, hours: List<WorkingHour> = emptyList()) =
        PointCloud(address, LatLng(3.0, 4.0), name, City, hours)

    private fun hours(from: String) = listOf(WorkingHour("Mon", -1, from, "15:00"))
}

private class FakePreference(var value: String?) : SelectedCityPreference {

    override suspend fun getPrefValue(): String = value ?: throw NoPreferenceException("fake")

    override suspend fun savePref(value: String) {
        this.value = value
    }

    override suspend fun updatePref(value: String) {
        this.value = value
    }
}

/**
 * Implements only the primitives of the DAO, so the logic written on top of them
 * (insertOrUpdate, deleteWithFavorites) is the real one.
 */
private class FakePointsDao : PointsDao {

    val favoritesRemovedFor = mutableListOf<Long>()

    private val points = mutableListOf<PointCache>()
    private val hours = mutableListOf<WorkingHourCache>()
    private var nextId = 1L

    override suspend fun pointsByCity(cityId: String) =
        points.filter { it.cityCodeId == cityId }.map { withHours(it) }

    override suspend fun pointsByCity(cityId: String, origin: PointOrigin) =
        points.filter { it.cityCodeId == cityId && it.origin == origin }.map { withHours(it) }

    override suspend fun pointById(id: Long) = points.first { it.pointId == id }

    override suspend fun pointId(cityId: String, address: String, origin: PointOrigin) =
        points.find { it.cityCodeId == cityId && it.address == address && it.origin == origin }
            ?.pointId

    override suspend fun count(pointId: Long, origin: PointOrigin) =
        points.count { it.pointId == pointId && it.origin == origin }

    override suspend fun deleteWorkingHours(pointId: Long) {
        hours.removeAll { it.pointId == pointId }
    }

    override suspend fun deleteFavorites(pointId: Long) {
        favoritesRemovedFor.add(pointId)
    }

    override suspend fun delete(pointId: Long, origin: PointOrigin): Int {
        val removed = points.removeAll { it.pointId == pointId && it.origin == origin }
        hours.removeAll { it.pointId == pointId }
        return if (removed) 1 else 0
    }

    override suspend fun insert(point: PointCache): Long {
        val id = nextId++
        points.add(point.copy(pointId = id))
        return id
    }

    override suspend fun insert(workingHourCache: WorkingHourCache) {
        hours.add(workingHourCache)
    }

    override suspend fun insert(point: List<PointCache>) {
        point.forEach { insert(it) }
    }

    override suspend fun delete(point: PointCache) {
        delete(point.pointId, point.origin)
    }

    override suspend fun update(point: PointCache) {
        val position = points.indexOfFirst { it.pointId == point.pointId }
        points[position] = point
    }

    private fun withHours(point: PointCache) =
        PointWithHours(point, hours.filter { it.pointId == point.pointId })
}
