package com.yuriisurzhykov.foodbanks.city.domain

import com.yuriisurzhykov.foodbanks.core.data.RepositoryResponse
import com.yuriisurzhykov.foodbanks.data.city.CityRepository
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.city.cache.CityWithPointsCache
import com.yuriisurzhykov.geo.data.LocalityGeocoder
import com.yuriisurzhykov.geo.data.LocationUnavailableException
import com.yuriisurzhykov.geo.data.UserLocationSource
import com.yuriisurzhykov.geo.LatLng
import com.yuriisurzhykov.foodbanks.data.prefs.CityModePreference
import com.yuriisurzhykov.foodbanks.data.prefs.NoPreferenceException
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import com.yuriisurzhykov.foodbanks.domain.city.City
import com.yuriisurzhykov.foodbanks.domain.city.CityCacheToDomainMapper
import com.yuriisurzhykov.foodbanks.domain.city.CityDetector
import com.yuriisurzhykov.foodbanks.domain.city.CityMatcher
import com.yuriisurzhykov.foodbanks.domain.city.CityNotDetectedException
import com.yuriisurzhykov.foodbanks.domain.city.SelectedCityUseCase
import com.yuriisurzhykov.geo.domain.DistanceCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SelectedCityUseCaseTest {

    private val sacramento = CityCache(1, "Sacramento", "sacramento", "CA", "USA", 38.58, -121.49)
    private val oakland = CityCache(2, "Oakland", "oakland", "CA", "USA", 37.80, -122.27)

    private lateinit var preference: FakeSelectedCityPreference
    private lateinit var mode: FakeCityModePreference
    private lateinit var cache: FakeCityCacheDataSource
    private lateinit var repository: FakeCityRepository
    private lateinit var location: FakeUserLocationSource
    private lateinit var geocoder: FakeCityGeocoder
    private lateinit var useCase: SelectedCityUseCase

    @Before
    fun `init resources for test`() {
        preference = FakeSelectedCityPreference()
        mode = FakeCityModePreference()
        cache = FakeCityCacheDataSource(mutableListOf(sacramento, oakland))
        repository = FakeCityRepository(cache, listOf(sacramento, oakland))
        location = FakeUserLocationSource(LatLng(38.6, -121.4))
        geocoder = FakeCityGeocoder("Sacramento")
        useCase = createUseCase()
    }

    @Test
    fun `test city chosen by user is used and nothing is detected`(): Unit = runBlocking {
        preference.value = oakland.nameCode

        assertEquals(oakland.nameCode, useCase.selectedCity().code())
        assertEquals(0, location.requests)
    }

    @Test
    fun `test manual choice wins over detected city`(): Unit = runBlocking {
        useCase.selectedCity()

        useCase.select(oakland.nameCode)

        assertEquals(oakland.nameCode, useCase.selectedCity().code())
        assertEquals(oakland.nameCode, preference.value)
    }

    @Test
    fun `test city is detected and remembered when nothing is chosen`(): Unit = runBlocking {
        assertEquals(sacramento.nameCode, useCase.selectedCity().code())
        assertEquals(sacramento.nameCode, preference.value)
    }

    @Test
    fun `test detected city is used from the next call without detecting again`(): Unit = runBlocking {
        useCase.selectedCity()
        useCase.selectedCity()

        assertEquals(1, location.requests)
    }

    @Test
    fun `test city is detected again when chosen city is no more in cache`(): Unit = runBlocking {
        preference.value = "removed-city"

        assertEquals(sacramento.nameCode, useCase.selectedCity().code())
        assertEquals(sacramento.nameCode, preference.value)
    }

    @Test
    fun `test nearest city is used when geocoder knows nothing`(): Unit = runBlocking {
        geocoder.locality = ""
        location.location = LatLng(37.78, -122.2)

        assertEquals(oakland.nameCode, useCase.selectedCity().code())
    }

    @Test
    fun `test empty cache is filled from the repository before detecting`(): Unit = runBlocking {
        cache.cities.clear()

        assertEquals(sacramento.nameCode, useCase.selectedCity().code())
        assertEquals(1, repository.fetches)
    }

    @Test
    fun `test failed detection is not remembered`(): Unit = runBlocking {
        location.location = null

        val failure = runCatching { useCase.selectedCity() }.exceptionOrNull()

        assertTrue(failure is CityNotDetectedException)
        assertEquals(null, preference.value)
    }

    @Test
    fun `test failed detection when user is far from all cities`(): Unit = runBlocking {
        geocoder.locality = ""
        location.location = LatLng(35.68, 139.69)

        val failure = runCatching { useCase.selectedCity() }.exceptionOrNull()

        assertTrue(failure is CityNotDetectedException)
    }

    @Test
    fun `test detecting by hand replaces the chosen city with the one where the user is`(): Unit = runBlocking {
        preference.value = oakland.nameCode

        assertEquals(sacramento.nameCode, useCase.detect().code())
        assertEquals(sacramento.nameCode, preference.value)
    }

    @Test
    fun `test detecting finds a city that appeared in the cloud after the cache was filled`(): Unit = runBlocking {
        val newCity = CityCache(3, "Folsom", "folsom", "CA", "USA", 38.67, -121.18)
        cache = FakeCityCacheDataSource(mutableListOf(sacramento, oakland))
        repository = FakeCityRepository(cache, listOf(sacramento, oakland, newCity))
        location.location = LatLng(38.67, -121.18)
        geocoder.locality = "Folsom"
        useCase = createUseCase()

        assertEquals(newCity.nameCode, useCase.detect().code())
    }

    @Test
    fun `test detecting uses the cached cities when the cloud cannot be reached`(): Unit = runBlocking {
        repository = FakeCityRepository(cache, listOf(sacramento, oakland), failing = true)
        useCase = createUseCase()

        assertEquals(sacramento.nameCode, useCase.detect().code())
    }

    @Test
    fun `test failed detecting by hand keeps the chosen city`(): Unit = runBlocking {
        preference.value = oakland.nameCode
        location.location = null

        val failure = runCatching { useCase.detect() }.exceptionOrNull()

        assertTrue(failure is CityNotDetectedException)
        assertEquals(oakland.nameCode, preference.value)
    }

    @Test
    fun `test a city is not automatic until the app took it by the location`(): Unit = runBlocking {
        assertFalse(useCase.isAutomatic())
    }

    @Test
    fun `test a city taken by the location is automatic`(): Unit = runBlocking {
        useCase.detect()

        assertTrue(useCase.isAutomatic())
    }

    @Test
    fun `test the city found on the first start is automatic`(): Unit = runBlocking {
        useCase.selectedCity()

        assertTrue(useCase.isAutomatic())
    }

    @Test
    fun `test a city picked by hand is no more automatic`(): Unit = runBlocking {
        useCase.detect()

        useCase.select(oakland.nameCode)

        assertFalse(useCase.isAutomatic())
    }

    @Test
    fun `test a failed detection keeps the mode of the chosen city`(): Unit = runBlocking {
        useCase.select(oakland.nameCode)
        location.location = null

        runCatching { useCase.detect() }

        assertFalse(useCase.isAutomatic())
    }

    private fun createUseCase(): SelectedCityUseCase {
        val detector = CityDetector.Base(
            location, geocoder, cache, repository, CityMatcher.Base(DistanceCalculator.Base())
        )
        return SelectedCityUseCase.Base(preference, mode, cache, detector, CityCacheToDomainMapper.Base())
    }

    private fun City.code(): String = map(object : City.Mapper<String> {
        override fun map(cityName: String, cityCode: String, country: String, region: String) =
            cityCode
    })
}

private class FakeCityModePreference : CityModePreference {

    var value: String? = null

    override suspend fun getPrefValue(): String = value ?: throw NoPreferenceException("fake")

    override suspend fun savePref(value: String) {
        this.value = value
    }

    override suspend fun updatePref(value: String) {
        this.value = value
    }
}

private class FakeSelectedCityPreference : SelectedCityPreference {

    var value: String? = null

    override suspend fun getPrefValue(): String = value ?: throw NoPreferenceException("fake")

    override suspend fun savePref(value: String) {
        this.value = value
    }

    override suspend fun updatePref(value: String) {
        this.value = value
    }
}

private class FakeCityCacheDataSource(val cities: MutableList<CityCache>) : CityCacheDataSource {

    override suspend fun cityByCode(codeName: String): CityWithPointsCache =
        CityWithPointsCache(cities.first { it.nameCode == codeName })

    override suspend fun city(codeName: String): CityCache? =
        cities.find { it.nameCode == codeName }

    override suspend fun cities(): List<CityCache> = cities.toList()

    override suspend fun insertAll(list: List<CityCache>) {
        cities.addAll(list)
    }
}

private class FakeCityRepository(
    private val cache: FakeCityCacheDataSource,
    private val cloud: List<CityCache>,
    private val failing: Boolean = false
) : CityRepository {

    var fetches = 0

    override suspend fun fetch(): Flow<RepositoryResponse<List<CityCache>>> = flow {
        fetches++
        if (failing) throw IllegalStateException("the cloud cannot be reached")
        cache.insertAll(cloud)
        emit(RepositoryResponse.Success(cache.cities()))
    }
}

private class FakeUserLocationSource(var location: LatLng?) : UserLocationSource {

    var requests = 0

    override suspend fun location(): LatLng {
        requests++
        return location ?: throw LocationUnavailableException("fake")
    }
}

private class FakeCityGeocoder(var locality: String) : LocalityGeocoder {

    override suspend fun locality(location: LatLng): String = locality
}
