package com.ktt.weatherforecast.domain.model



import java.util.Date

data class DailyForecast(
    val time: Date,
    val minTemp: Double,
    val maxTemp: Double,
    val condition: String,
    val icon: String // OpenWeatherMap icon code, e.g., "10d"
)
