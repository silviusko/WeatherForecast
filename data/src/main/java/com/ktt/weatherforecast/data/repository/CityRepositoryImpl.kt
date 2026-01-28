package com.ktt.weatherforecast.data.repository

import com.ktt.weatherforecast.data.datasource.LocalCityDataSource
import com.ktt.weatherforecast.data.datasource.RemoteCityDataSource
import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val localDataSource: LocalCityDataSource,
    private val remoteDataSource: RemoteCityDataSource
) : CityRepository {

    override fun searchCities(query: String): Flow<List<City>> {
        // Local
        // To disable local database, comment out the following block and uncomment the empty flow below
        val localFlow = localDataSource.searchCities(query)
        // val localFlow = flowOf(emptyList<City>())

        // Remote
        // To disable remote API, comment out the following block and uncomment the empty flow below
//        val remoteFlow = flow {
//            if (query.isBlank()) {
//                emit(emptyList<City>())
//            } else {
//                val remoteCities = remoteDataSource.searchCities(query)
//                emit(remoteCities)
//            }
//        }
         val remoteFlow = flowOf(emptyList<City>())

        // Combine
        return combine(localFlow, remoteFlow) { local, remote ->
            (remote + local).distinctBy { "${it.name},${it.country}" }
        }
    }

    override suspend fun getSavedCity(): City? {
        return localDataSource.getSavedCity()
    }

    override suspend fun getCityByName(name: String): City? {
        return localDataSource.getCityByName(name)
    }

    override suspend fun saveCity(city: City) {
        localDataSource.saveCity(city)
    }

    override suspend fun ensureCityDataInitialized() {
        localDataSource.ensureCityDataInitialized()
    }
}
