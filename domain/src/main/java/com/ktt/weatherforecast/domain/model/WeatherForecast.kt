package com.ktt.weatherforecast.domain.model

data class WeatherForecast(
    val city: City,
    val current: DailyForecast,
    val hourly: List<DailyForecast>,
    val weekly: List<DailyForecast>
)
