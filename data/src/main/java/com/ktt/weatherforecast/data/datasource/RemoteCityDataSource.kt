package com.ktt.weatherforecast.data.datasource

import com.ktt.weatherforecast.data.remote.WeatherApiService
import com.ktt.weatherforecast.domain.model.City
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

interface RemoteCityDataSource {
    suspend fun searchCities(query: String): List<City>
}

class RemoteCityDataSourceImpl @Inject constructor(
    private val apiService: WeatherApiService,
    @Named("open_weather_api_key") private val apiKey: String
) : RemoteCityDataSource {

    override suspend fun searchCities(query: String): List<City> {
        return try {
            val dtos = apiService.getCoordinates(query, 5, apiKey)
            val currentLanguage = Locale.getDefault().language // e.g., "en", "zh"
            
            dtos.map { dto ->
                // "If api returns local_name, please use local name"
                // We try specific language first (e.g. "zh"), then fallback to "name" (which is usually English/Default)
                val localizedName = dto.localNames?.get(currentLanguage)
                
                // Synthetic ID
                val syntheticId = "${dto.name}${dto.lat}${dto.lon}".hashCode().toLong()
                
                City(
                    id = syntheticId,
                    name = dto.name,
                    localName = localizedName,
                    country = dto.country,
                    lat = dto.lat,
                    lon = dto.lon
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
