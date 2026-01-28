package com.ktt.weatherforecast.domain.repository

import com.ktt.weatherforecast.domain.model.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherForecast(cityId: Long, lat: Double, lon: Double): Result<WeatherForecast>
}
