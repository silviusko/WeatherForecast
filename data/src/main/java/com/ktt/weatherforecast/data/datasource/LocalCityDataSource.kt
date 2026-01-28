package com.ktt.weatherforecast.data.datasource

import android.content.Context
import androidx.room.withTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ktt.weatherforecast.data.local.AppDatabase
import com.ktt.weatherforecast.data.local.dao.CityDao
import com.ktt.weatherforecast.data.local.entity.CityEntity
import com.ktt.weatherforecast.data.local.entity.toCity
import com.ktt.weatherforecast.data.local.entity.toEntity
import com.ktt.weatherforecast.domain.model.City
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject

interface LocalCityDataSource {
    fun searchCities(query: String): Flow<List<City>>
    suspend fun getSavedCity(): City?
    suspend fun getCityByName(name: String): City?
    suspend fun saveCity(city: City)
    suspend fun ensureCityDataInitialized()
}

class LocalCityDataSourceImpl @Inject constructor(
    private val cityDao: CityDao,
    private val db: AppDatabase,
    @ApplicationContext private val context: Context
) : LocalCityDataSource {

    override fun searchCities(query: String): Flow<List<City>> {
        return cityDao.searchCities(query).map { entities ->
            entities.map { it.toCity() }
        }
    }

    override suspend fun getSavedCity(): City? {
        return cityDao.getSavedCity()?.toCity()
    }

    override suspend fun getCityByName(name: String): City? {
        return cityDao.getCityByName(name)?.toCity()
    }

    override suspend fun saveCity(city: City) {
        db.withTransaction {
            cityDao.clearSavedCity()
            val entity = city.toEntity(isSaved = true)
            cityDao.insertCity(entity)
        }
    }

    override suspend fun ensureCityDataInitialized() {
        withContext(Dispatchers.IO) {
            val count = cityDao.getCityCount()
            if (count == 0) {
                try {
                    val inputStream = context.assets.open("city.list.min.json")
                    val reader = InputStreamReader(inputStream)
                    val type = object : TypeToken<List<CityFileDto>>() {}.type
                    val cityList: List<CityFileDto> = Gson().fromJson(reader, type)

                    val entities = cityList.map { dto ->
                        CityEntity(
                            id = dto.id,
                            name = dto.name,
                            country = dto.country,
                            lat = dto.coord.lat,
                            lon = dto.coord.lon,
                            isSaved = false
                        )
                    }
                    
                    val chunkSize = 1000
                    entities.chunked(chunkSize).forEach { batch ->
                         cityDao.insertCities(batch)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    // Helper DTOs for local JSON parsing
    private data class CityFileDto(
        val id: Long,
        val name: String,
        val country: String,
        val coord: CoordDto
    )

    private data class CoordDto(
        val lon: Double,
        val lat: Double
    )
}
