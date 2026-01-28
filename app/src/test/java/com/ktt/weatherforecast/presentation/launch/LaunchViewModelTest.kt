package com.ktt.weatherforecast.presentation.launch

import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.repository.CityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchViewModelTest {

    private lateinit var viewModel: LaunchViewModel
    private val cityRepository: CityRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when saved city exists, navigate to weather`() = runTest {
        val savedCity = City(1, "London", "London", "UK", 51.5, -0.1)
        coEvery { cityRepository.getSavedCity() } returns savedCity
        
        viewModel = LaunchViewModel(cityRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is LaunchUiState.NavigateToWeather)
        assertEquals("London", (state as LaunchUiState.NavigateToWeather).cityName)
    }

    @Test
    fun `when no saved city, navigate to city list`() = runTest {
        coEvery { cityRepository.getSavedCity() } returns null
        
        viewModel = LaunchViewModel(cityRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is LaunchUiState.NavigateToCityList)
    }
    
    @Test
    fun `init ensures city data initialized`() = runTest {
        viewModel = LaunchViewModel(cityRepository)
        advanceUntilIdle()
        
        coVerify { cityRepository.ensureCityDataInitialized() }
    }
}
