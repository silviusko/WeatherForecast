package com.ktt.weatherforecast.presentation.weather

import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.model.WeatherForecast
import com.ktt.weatherforecast.domain.repository.CityRepository
import com.ktt.weatherforecast.domain.usecase.GetWeatherForecastUseCase
import io.mockk.coEvery
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
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase = mockk()
    private val cityRepository: CityRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(getWeatherForecastUseCase, cityRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadWeather with invalid city returns error`() = runTest {
        coEvery { cityRepository.getCityByName("Unknown") } returns null

        viewModel.loadWeather("Unknown")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is WeatherUiState.Error)
    }

    @Test
    fun `loadWeather success returns success state`() = runTest {
        val city = City(1, "London", "London", "UK", 51.5, -0.1)
        val daily = com.ktt.weatherforecast.domain.model.DailyForecast(
            time = java.util.Date(),
            minTemp = 10.0,
            maxTemp = 20.0,
            condition = "Sunny",
            icon = "01d"
        )
        val forecast = WeatherForecast(
            city = city,
            current = daily,
            hourly = emptyList(),
            weekly = emptyList()
        )
        
        coEvery { cityRepository.getCityByName("London") } returns city
        coEvery { getWeatherForecastUseCase(city) } returns Result.success(forecast)

        viewModel.loadWeather("London")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is WeatherUiState.Success)
        assertEquals("Sunny", (state as WeatherUiState.Success).forecast.current.condition)
    }
    
    @Test
    fun `loadWeather failure returns error state`() = runTest {
        val city = City(1, "London", "London", "UK", 51.5, -0.1)
        
        coEvery { cityRepository.getCityByName("London") } returns city
        coEvery { getWeatherForecastUseCase(city) } returns Result.failure(Exception("Network error"))

        viewModel.loadWeather("London")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is WeatherUiState.Error)
        assertEquals("Network error", (state as WeatherUiState.Error).message)
    }
}
