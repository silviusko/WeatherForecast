package com.ktt.weatherforecast.domain.usecase

import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.repository.CityRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchCitiesUseCaseTest {

    private val repository = mockk<CityRepository>()
    private val useCase = SearchCitiesUseCase(repository)

    @Test
    fun `invoke calls repository searchCities`() = runTest {
        // Given
        val query = "London"
        val cities = listOf(City(1, "London", null, "UK", 51.5, -0.1))
        every { repository.searchCities(query) } returns flowOf(cities)

        // When
        val result = useCase(query).first()

        // Then
        assertEquals(1, result.size)
        assertEquals("London", result[0].name)
        verify { repository.searchCities(query) }
    }
}
