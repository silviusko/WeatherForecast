package com.ktt.weatherforecast.presentation.city_list

import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.repository.CityRepository
import com.ktt.weatherforecast.domain.usecase.SearchCitiesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class CityListViewModelTest {

    private lateinit var viewModel: CityListViewModel
    private val searchCitiesUseCase: SearchCitiesUseCase = mockk()
    private val cityRepository: CityRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CityListViewModel(searchCitiesUseCase, cityRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() = runTest {
        val cities = viewModel.cities.value
        assertTrue(cities.isEmpty())
    }

    @Test
    fun `search with short query returns empty list`() = runTest {
        viewModel.onSearchQueryChanged("a")
        advanceTimeBy(1100)
        
        val cities = viewModel.cities.value
        assertTrue(cities.isEmpty())
        
        coVerify(exactly = 0) { searchCitiesUseCase(any()) }
    }

    @Test
    fun `search with valid query returns cities`() = runTest {
        val expectedCities = listOf(
            City(1, "London", "London", "UK", 51.5, -0.1)
        )
        coEvery { searchCitiesUseCase("London") } returns flowOf(expectedCities)

        viewModel.onSearchQueryChanged("London")
        advanceTimeBy(1100)

        val cities = viewModel.cities.value
        assertEquals(expectedCities, cities)
    }

    @Test
    fun `onCitySelected saves city`() = runTest {
        val city = City(1, "London", "London", "UK", 51.5, -0.1)
        
        viewModel.onCitySelected(city)
        advanceTimeBy(100) // Ensure coroutine runs

        coVerify { cityRepository.saveCity(city) }
    }
}
