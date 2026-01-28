package com.ktt.weatherforecast.data.repository

import com.ktt.weatherforecast.data.datasource.LocalCityDataSource
import com.ktt.weatherforecast.data.datasource.RemoteCityDataSource
import com.ktt.weatherforecast.domain.model.City
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CityRepositoryImplTest {

    @MockK
    lateinit var localDataSource: LocalCityDataSource

    @MockK
    lateinit var remoteDataSource: RemoteCityDataSource

    private lateinit var repository: CityRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CityRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `searchCities combines local and remote`() = runBlocking {
        // Given
        val query = "London"
        val localCities = listOf(
            City(1, "London", null, "UK", 51.5, -0.1)
        )
        val remoteCities = listOf(
            City(2, "Londonderry", null, "UK", 54.9, -7.3)
        )
        
        coEvery { localDataSource.searchCities(query) } returns flowOf(localCities)
        coEvery { remoteDataSource.searchCities(query) } returns remoteCities

        // When
        val result = repository.searchCities(query).first()

        // Then
        // Should contain unique cities
        assertEquals(2, result.size)
        
        coVerify { localDataSource.searchCities(query) }
        coVerify { remoteDataSource.searchCities(query) }
    }

    @Test
    fun `getSavedCity delegates to localDataSource`() = runBlocking {
        // Given
        val city = City(1, "London", null, "UK", 51.5, -0.1)
        coEvery { localDataSource.getSavedCity() } returns city

        // When
        val result = repository.getSavedCity()

        // Then
        assertEquals("London", result?.name)
        coVerify { localDataSource.getSavedCity() }
    }

    @Test
    fun `getCityByName delegates to localDataSource`() = runBlocking {
        // Given
        val cityName = "London"
        val city = City(1, "London", null, "UK", 51.5, -0.1)
        coEvery { localDataSource.getCityByName(cityName) } returns city
        
        // When
        val result = repository.getCityByName(cityName)
        
        // Then
        assertEquals("London", result?.name)
        coVerify { localDataSource.getCityByName(cityName) }
    }

    @Test
    fun `ensureCityDataInitialized delegates to localDataSource`() = runBlocking {
        // Given
        coEvery { localDataSource.ensureCityDataInitialized() } just Runs

        // When
        repository.ensureCityDataInitialized()

        // Then
        coVerify { localDataSource.ensureCityDataInitialized() }
    }
}
