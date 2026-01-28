package com.ktt.weatherforecast.domain.usecase

import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.model.WeatherForecast
import com.ktt.weatherforecast.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetWeatherForecastUseCaseTest {

    private val repository = mockk<WeatherRepository>()
    private val useCase = GetWeatherForecastUseCase(repository)

    @Test
    fun `invoke calls repository and returns success`() = runBlocking {
        // Given
        val city = City(1, "London", null, "UK", 51.5, -0.1)
        val expectedForecast = mockk<WeatherForecast>()
        coEvery { repository.getWeatherForecast(1, 51.5, -0.1) } returns Result.success(expectedForecast)

        // When
        val result = useCase(city)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedForecast, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when repository fails`() = runBlocking {
        // Given
        val city = City(1, "London", null, "UK", 51.5, -0.1)
        val exception = Exception("Network Error")
        coEvery { repository.getWeatherForecast(1, 51.5, -0.1) } returns Result.failure(exception)

        // When
        val result = useCase(city)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
