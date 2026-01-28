package com.ktt.weatherforecast.domain.repository

import com.ktt.weatherforecast.domain.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun searchCities(query: String): Flow<List<City>>
    suspend fun getSavedCity(): City?
    suspend fun saveCity(city: City)
    suspend fun ensureCityDataInitialized()
    suspend fun getCityByName(name: String): City?
}
