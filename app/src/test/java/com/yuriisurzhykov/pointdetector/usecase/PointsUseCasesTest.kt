package com.yuriisurzhykov.pointdetector.usecase

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.cache.PointOrigin
import com.yuriisurzhykov.foodbanks.domain.city.City
import com.yuriisurzhykov.foodbanks.domain.city.CityNotDetectedException
import com.yuriisurzhykov.foodbanks.domain.city.SelectedCityUseCase
import com.yuriisurzhykov.foodbanks.domain.favorites.Favorite
import com.yuriisurzhykov.foodbanks.domain.favorites.FavoritesUseCase
import com.yuriisurzhykov.foodbanks.domain.points.Point
import com.yuriisurzhykov.foodbanks.domain.points.PointHelp
import com.yuriisurzhykov.foodbanks.domain.points.PointsListUseCase
import com.yuriisurzhykov.foodbanks.domain.points.UserPointsUseCase
import com.yuriisurzhykov.geo.LatLng
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng as UiLatLng
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesApplyUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesFetchUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesRemoveUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAllPointsUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SearchPointUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.yuriisurzhykov.pointdetector.domain.entities.Point as UserPoint
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay

class PointsUseCasesTest {

    private lateinit var city: FakeSelectedCity
    private lateinit var pointsList: FakePointsList
    private lateinit var fetchAll: FetchAllPointsUseCase

    @Before
    fun `init resources for test`() {
        city = FakeSelectedCity()
        pointsList = FakePointsList(
            listOf(UseCaseResponse.Success(listOf(point(1, "Sunrise Food", "San Juan Ave"))))
        )
        fetchAll = FetchAllPointsUseCase.Base(city, pointsList, FakeUiMapper())
    }

    @Test
    fun `test points of the city are shown`(): Unit = runBlocking {
        val lists = fetchAll.fetchPoints().toList()

        assertEquals(listOf(listOf("San Juan Ave")), lists.map { list -> list.map { it.address } })
    }

    @Test
    fun `test cached and then cloud values are both shown`(): Unit = runBlocking {
        pointsList.responses = listOf(
            UseCaseResponse.Success(listOf(point(1, "Old", "A"))),
            UseCaseResponse.Success(listOf(point(1, "Old", "A"), point(2, "New", "B")))
        )

        val lists = fetchAll.fetchPoints().toList()

        assertEquals(listOf(1, 2), lists.map { it.size })
    }

    @Test
    fun `test no network still shows cached points`(): Unit = runBlocking {
        pointsList.responses = listOf(UseCaseResponse.NoNetwork(listOf(point(1, "Old", "A"))))

        assertEquals(listOf(1), fetchAll.fetchPoints().toList().map { it.size })
    }

    @Test
    fun `test network error does not replace cached points with nothing`(): Unit = runBlocking {
        pointsList.responses = listOf(
            UseCaseResponse.Success(listOf(point(1, "Old", "A"))),
            UseCaseResponse.NetworkError()
        )

        assertEquals(listOf(1), fetchAll.fetchPoints().toList().map { it.size })
    }

    @Test
    fun `test no city means no points and the points are not even requested`(): Unit = runBlocking {
        city.detected = false

        val lists = fetchAll.fetchPoints().toList()

        assertEquals(listOf(emptyList<PointUi>()), lists)
        assertEquals(0, pointsList.requests)
    }

    @Test
    fun `test search by name and by address ignores case`(): Unit = runBlocking {
        pointsList.responses = listOf(
            UseCaseResponse.Success(
                listOf(
                    point(1, "Sunrise Food", "San Juan Ave"),
                    point(2, "Church", "Pecan Ave"),
                    point(3, "Locker", "Locust Rd")
                )
            )
        )
        val search = SearchPointUseCase.Base(fetchAll)

        assertEquals(listOf("San Juan Ave"), search.addresses("SUNRISE"))
        assertEquals(listOf("San Juan Ave", "Pecan Ave"), search.addresses("ave"))
        assertEquals(emptyList<String>(), search.addresses("nothing"))
    }

    @Test
    fun `test only favorites are shown in favorites`(): Unit = runBlocking {
        pointsList.responses = listOf(
            UseCaseResponse.Success(
                listOf(point(1, "A", "a", favorite = true), point(2, "B", "b"))
            )
        )

        val favorites = FavoritesFetchUseCase.Base(fetchAll).fetchFavorites().toList().single()

        assertEquals(listOf("a"), favorites.map { it.address })
    }

    @Test
    fun `test apply favorite switches the favorite of the point by id`() {
        val favorites = FakeFavorites()
        val apply = FavoritesApplyUseCase.Base(favorites)

        apply.applyFavorite(pointUi(7, favorite = false))
        apply.applyFavorite(pointUi(8, favorite = true))

        assertEquals(listOf(7L), favorites.marked)
        assertEquals(listOf(8L), favorites.unmarked)
    }

    @Test
    fun `test remove favorite unmarks the point by id`() {
        val favorites = FakeFavorites()

        FavoritesRemoveUseCase.Base(favorites).remove(pointUi(9, favorite = true))

        assertEquals(listOf(9L), favorites.unmarked)
    }

    @Test
    fun `test point created by the user is saved to the selected city as user point`(): Unit =
        runBlocking {
            val userPoints = FakeUserPoints()
            val save = SavePointUseCase.Base(city, userPoints)

            save.save(
                UserPoint(
                    "My address",
                    UiLatLng(1.5, 2.5),
                    "My place",
                    listOf(WeekDay("Mon", 1, "09:00", "12:00"))
                )
            )

            val added = userPoints.added.single()
            assertEquals("My place", added.placeName)
            assertEquals("My address", added.address)
            assertEquals(LatLng(1.5, 2.5), added.coordinates)
            assertEquals(listOf(WorkingHour("Mon", 1, "09:00", "12:00")), added.workingHours)
        }

    @Test
    fun `test point is not saved when there is no city`(): Unit = runBlocking {
        city.detected = false
        val userPoints = FakeUserPoints()

        val failure = runCatching {
            SavePointUseCase.Base(city, userPoints).save(UserPoint("a", UiLatLng(), "n", emptyList()))
        }.exceptionOrNull()

        assertTrue(failure is CityNotDetectedException)
        assertTrue(userPoints.added.isEmpty())
    }

    private suspend fun SearchPointUseCase.addresses(condition: String): List<String> =
        searchPlaceByCondition(condition).toList().last().map { it.address }

    private fun point(id: Long, name: String, address: String, favorite: Boolean = false) =
        Point.Base(
            id, name, address, LatLng(0.0, 0.0), emptyList(), PointOrigin.CLOUD, favorite
        )

    private fun pointUi(id: Long, favorite: Boolean) = PointUi(
        address = "a$id",
        coordinates = UiLatLng(),
        distanceString = "",
        distanceDouble = 0.0,
        placeName = "n$id",
        favoriteState = FavoriteState.Factory.build(favorite),
        id = id
    )
}

private class FakeSelectedCity : SelectedCityUseCase {

    var detected = true

    override suspend fun selectedCity(): City {
        if (!detected) throw CityNotDetectedException("fake")
        return City.Base("Sacramento", "sacramento", "USA", "CA")
    }

    override suspend fun select(cityCode: String) = Unit

    override suspend fun detect(): City = selectedCity()
}

private class FakePointsList(var responses: List<UseCaseResponse<List<Point>>>) :
    PointsListUseCase {

    var requests = 0

    override suspend fun points(): Flow<UseCaseResponse<List<Point>>> {
        requests++
        return flow { responses.forEach { emit(it) } }
    }

    override suspend fun markFavorite(point: Point, coroutineScope: CoroutineScope) = Unit
}

private class FakeUiMapper : SuspendMapper<List<Point>, List<PointUi>> {

    override suspend fun map(from: List<Point>): List<PointUi> = from.map { point ->
        point.map(object : Point.Mapper<PointUi> {
            override fun map(
                id: Long,
                name: String,
                address: String,
                coordinates: LatLng,
                workingHours: List<WorkingHour>,
                origin: PointOrigin,
                favorite: Boolean,
                help: PointHelp
            ) = PointUi(
                address = address,
                coordinates = UiLatLng(coordinates.lat, coordinates.lng),
                distanceString = "",
                distanceDouble = 0.0,
                placeName = name,
                favoriteState = FavoriteState.Factory.build(favorite),
                id = id
            )
        })
    }
}

private class FakeFavorites : FavoritesUseCase {

    val marked = mutableListOf<Long>()
    val unmarked = mutableListOf<Long>()

    override suspend fun favorites(): Flow<List<Favorite>> = flow { emit(emptyList()) }

    override suspend fun detachFavorite(favorite: Favorite, coroutineScope: CoroutineScope) = Unit

    override suspend fun markFavorite(pointId: Long): Boolean = marked.add(pointId)

    override suspend fun unmarkFavorite(pointId: Long): Boolean = unmarked.add(pointId)
}

private class FakeUserPoints : UserPointsUseCase {

    class Added(
        val placeName: String,
        val address: String,
        val coordinates: LatLng,
        val workingHours: List<WorkingHour>
    )

    val added = mutableListOf<Added>()

    override suspend fun add(
        placeName: String,
        address: String,
        coordinates: LatLng,
        workingHours: List<WorkingHour>
    ) {
        added.add(Added(placeName, address, coordinates, workingHours))
    }

    override suspend fun remove(pointId: Long): Boolean = false
}
