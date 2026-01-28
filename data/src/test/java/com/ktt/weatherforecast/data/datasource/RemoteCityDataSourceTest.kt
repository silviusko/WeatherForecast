package com.ktt.weatherforecast.data.datasource

import com.ktt.weatherforecast.data.remote.WeatherApiService
import com.ktt.weatherforecast.data.remote.dto.GeocodingDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteCityDataSourceTest {

    private lateinit var dataSource: RemoteCityDataSource
    private val apiService: WeatherApiService = mockk()
    private val apiKey = "test_api_key"

    @Before
    fun setUp() {
        dataSource = RemoteCityDataSourceImpl(apiService, apiKey)
    }

    @Test
    fun `searchCities returns mapped cities from geocoding dto`() = runTest {
        val query = "London"
        val dtoList = listOf(
            GeocodingDto(
                name = "London",
                lat = 51.5,
                lon = -0.1,
                country = "GB",
                localNames = mapOf("en" to "London"),
                state = "England"
            )
        )
        
        coEvery { apiService.getCoordinates(query, 5, apiKey) } returns dtoList

        val result = dataSource.searchCities(query)

        assertEquals(1, result.size)
        assertEquals("London", result[0].name)
        assertEquals(51.5, result[0].lat, 0.0)
    }

    @Test
    fun `searchCities returns empty list on error`() = runTest {
        val query = "Error"
        coEvery { apiService.getCoordinates(query, 5, apiKey) } throws Exception("Network error")

        val result = dataSource.searchCities(query)

        assertTrue(result.isEmpty())
    }
}
