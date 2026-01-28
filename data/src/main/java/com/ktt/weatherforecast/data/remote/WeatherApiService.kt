package com.ktt.weatherforecast.data.remote

import com.ktt.weatherforecast.data.remote.dto.CurrentWeatherDto
import com.ktt.weatherforecast.data.remote.dto.FiveDayForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("id") cityId: Long,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw"
    ): CurrentWeatherDto

    // Alternative: Get by Lat/Lon if prefered, but we use City ID for precision if available
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCoords(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw"
    ): CurrentWeatherDto

    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("id") cityId: Long,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw"
    ): FiveDayForecastDto
    
    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecastByCoords(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw"
    ): FiveDayForecastDto

    @GET("geo/1.0/direct")
    suspend fun getCoordinates(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<com.ktt.weatherforecast.data.remote.dto.GeocodingDto>
}
