package com.ktt.weatherforecast.domain.usecase

import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.model.WeatherForecast
import com.ktt.weatherforecast.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: City): Result<WeatherForecast> {
        return repository.getWeatherForecast(city.id, city.lat, city.lon)
    }
}
