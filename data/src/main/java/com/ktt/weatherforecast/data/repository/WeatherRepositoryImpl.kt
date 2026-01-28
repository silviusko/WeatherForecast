package com.ktt.weatherforecast.data.repository

import com.ktt.weatherforecast.data.mapper.toHourlyForecast
import com.ktt.weatherforecast.data.mapper.toWeatherForecast
import com.ktt.weatherforecast.data.mapper.toWeeklyForecast
import com.ktt.weatherforecast.data.remote.WeatherApiService
import com.ktt.weatherforecast.domain.model.WeatherForecast
import com.ktt.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService,
    @Named("open_weather_api_key") private val apiKey: String
) : WeatherRepository {

    override suspend fun getWeatherForecast(cityId: Long, lat: Double, lon: Double): Result<WeatherForecast> {
        return withContext(Dispatchers.IO) {
            try {
                // Determine language based on system locale
                val lang = getSystemLanguageCode()
                
                // Fetch Current Weather
                // Use coordinates if available for better precision, or cityId
                val currentDto = apiService.getCurrentWeatherByCoords(lat, lon, apiKey, lang = lang)
                
                // Fetch 5 Day Forecast
                val forecastDto = apiService.getFiveDayForecastByCoords(lat, lon, apiKey, lang = lang)

                // Map to Domain
                val domainForecast = currentDto.toWeatherForecast().copy(
                    weekly = forecastDto.toWeeklyForecast(),
                    hourly = forecastDto.toHourlyForecast()
                )

                Result.success(domainForecast)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

    private fun getSystemLanguageCode(): String {
        val locale = java.util.Locale.getDefault()
        return if (locale.language == "zh" && locale.country == "TW") {
            "zh_tw"
        } else {
            "en"
        }
    }
}
