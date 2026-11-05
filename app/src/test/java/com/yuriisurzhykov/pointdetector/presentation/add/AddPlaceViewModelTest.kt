package com.yuriisurzhykov.pointdetector.presentation.add

import com.yuriisurzhykov.foodbanks.domain.city.CityNotDetectedException
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessage
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessages
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SuggestedPlacesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek

@OptIn(ExperimentalCoroutinesApi::class)
class AddPlaceViewModelTest {

    private val test = UnconfinedTestDispatcher()
    private val dispatchers = object : Dispatchers.Abstract(test, test) {}
    private val suggested = FakeSuggestions()
    private val saver = FakeSaver()
    private val messages = AppMessages()
    private val told = mutableListOf<AppMessage>()
    private lateinit var viewModel: AddPlaceViewModel

    private val capitol = Point("2210 Capitol Ave", LatLng(38.57, -121.47), "Capitol Pantry")

    @Before
    fun setUp() {
        kotlinx.coroutines.Dispatchers.setMain(test)
        CoroutineScope(test).launch { messages.events.collect { told += it } }
        viewModel = AddPlaceViewModel(suggested, saver, dispatchers, messages)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    private fun fillHours() {
        viewModel.onOpenHours()
        viewModel.onDay(DayOfWeek.MONDAY)
        viewModel.onAddHours()
        viewModel.onCloseHours()
    }

    @Test
    fun `test typing finds places after a pause`() = runTest(test) {
        suggested.found = listOf(capitol)

        viewModel.onQuery("2210")
        assertEquals(Suggestions.Loading, viewModel.state.value.suggestions)
        advanceUntilIdle()

        assertEquals(Suggestions.Found(listOf(capitol)), viewModel.state.value.suggestions)
    }

    @Test
    fun `test nothing found and a blank query are different states`() = runTest(test) {
        viewModel.onQuery("pantri")
        advanceUntilIdle()
        assertEquals(Suggestions.NotFound, viewModel.state.value.suggestions)

        viewModel.onQuery("")
        assertEquals(Suggestions.Idle, viewModel.state.value.suggestions)
    }

    @Test
    fun `test picking a place fills the form and goes to the second step`() {
        viewModel.onPick(capitol)

        val state = viewModel.state.value
        assertEquals(AddStep.Details, state.step)
        assertEquals("Capitol Pantry", state.name)
        assertEquals("2210 Capitol Ave", state.address)
    }

    @Test
    fun `test adding by hand starts with the typed text as the address`() = runTest(test) {
        viewModel.onQuery("somewhere")
        viewModel.onManual()

        assertEquals(AddStep.Details, viewModel.state.value.step)
        assertEquals("somewhere", viewModel.state.value.address)
        assertEquals("", viewModel.state.value.name)
    }

    @Test
    fun `test saving without hours shows the error and saves nothing`() {
        viewModel.onPick(capitol)

        viewModel.onSave()

        assertTrue(viewModel.state.value.hoursError)
        assertTrue(saver.saved.isEmpty())
    }

    @Test
    fun `test a saved place goes to the result with only the answered facts`() {
        viewModel.onPick(capitol)
        fillHours()
        viewModel.onAnswer("appointment", FactAnswer.Yes)
        viewModel.onAnswer("documents", FactAnswer.Unknown)

        viewModel.onSave()

        val state = viewModel.state.value
        assertEquals(AddStep.Done, state.step)
        assertEquals("Capitol Pantry", state.saved?.name)
        val point = saver.saved.single()
        assertEquals(mapOf("appointment" to true), point.help)
        assertEquals(listOf("Mon"), point.workingHours.map { it.dayName })
        assertEquals(capitol.coordinates, point.coordinates)
        assertEquals(listOf<AppMessage>(AppMessage.PlaceAdded("Capitol Pantry")), told)
    }

    @Test
    fun `test a changed address is found again instead of keeping old coordinates`() {
        viewModel.onPick(capitol)
        fillHours()
        viewModel.onAddress("Another Street 5")
        suggested.found = listOf(Point("Another Street 5", LatLng(1.0, 2.0), "x"))

        viewModel.onSave()

        assertEquals(LatLng(1.0, 2.0), saver.saved.single().coordinates)
    }

    @Test
    fun `test an address nobody finds is not saved`() {
        viewModel.onManual()
        viewModel.onAddress("nowhere")
        fillHours()

        viewModel.onSave()

        assertEquals(SaveProblem.AddressNotFound, viewModel.state.value.problem)
        assertEquals(listOf<AppMessage>(AppMessage.PlaceNotSaved(AppMessage.PlaceNotSaved.Reason.AddressNotFound)), told)
        assertFalse(viewModel.state.value.saving)
        assertTrue(saver.saved.isEmpty())
    }

    @Test
    fun `test no city is reported and the form stays`() {
        viewModel.onPick(capitol)
        fillHours()
        saver.failure = CityNotDetectedException("no city")

        viewModel.onSave()

        assertEquals(SaveProblem.CityNotSelected, viewModel.state.value.problem)
        assertEquals(listOf<AppMessage>(AppMessage.PlaceNotSaved(AppMessage.PlaceNotSaved.Reason.CityNotSelected)), told)
        assertEquals(AddStep.Details, viewModel.state.value.step)
        assertFalse(viewModel.state.value.saving)
    }

    @Test
    fun `test back closes the sheets first, then returns to the search, then leaves the flow`() {
        viewModel.onPick(capitol)
        viewModel.onOpenHours()
        viewModel.onCitySheet(true)

        assertTrue(viewModel.onBack())
        assertFalse(viewModel.state.value.citySheet)
        assertTrue(viewModel.onBack())
        assertNull(viewModel.state.value.draft)
        assertTrue(viewModel.onBack())
        assertEquals(AddStep.Search, viewModel.state.value.step)
        assertFalse(viewModel.onBack())
    }

    @Test
    fun `test another place starts from scratch`() {
        viewModel.onPick(capitol)
        fillHours()
        viewModel.onSave()

        viewModel.onAnother()

        assertEquals(AddPlaceState(), viewModel.state.value)
    }

    private class FakeSuggestions : SuggestedPlacesUseCase {
        var found: List<Point> = emptyList()
        override suspend fun getSuggestedPlaces(placeName: String): List<Point> = found
    }

    private class FakeSaver : SavePointUseCase {
        val saved = mutableListOf<Point>()
        var failure: Exception? = null
        override suspend fun save(point: Point) {
            failure?.let { throw it }
            saved += point
        }
    }
}
