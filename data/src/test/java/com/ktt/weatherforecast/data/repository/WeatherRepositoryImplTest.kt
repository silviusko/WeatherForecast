package com.ktt.weatherforecast.data.repository

import com.ktt.weatherforecast.data.remote.WeatherApiService
import com.ktt.weatherforecast.data.remote.dto.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherRepositoryImplTest {

    private val apiService = mockk<WeatherApiService>()
    private val repository = WeatherRepositoryImpl(apiService, "fake_api_key")

    @Test
    fun `getWeatherForecast returns success when API calls succeed`() = runBlocking {
        // Given
        val lat = 51.5
        val lon = -0.1
        val cityId = 1L
        
        // Mock DTOs (simplified for brevity)
        val currentDto = mockk<CurrentWeatherDto>(relaxed = true)
        val forecastDto = mockk<FiveDayForecastDto>(relaxed = true)
        
        coEvery { apiService.getCurrentWeatherByCoords(lat, lon, any(), any(), any()) } returns currentDto
        coEvery { apiService.getFiveDayForecastByCoords(lat, lon, any(), any(), any()) } returns forecastDto

        // When
        val result = repository.getWeatherForecast(cityId, lat, lon)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getWeatherForecast returns failure when API call fails`() = runBlocking {
        // Given
        val lat = 51.5
        val lon = -0.1
        
        coEvery { apiService.getCurrentWeatherByCoords(any(), any(), any(), any(), any()) } throws RuntimeException("API Error")

        // When
        val result = repository.getWeatherForecast(1, lat, lon)

        // Then
        assertTrue(result.isFailure)
    }
}
