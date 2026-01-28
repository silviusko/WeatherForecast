package com.ktt.weatherforecast.presentation.model

import com.ktt.weatherforecast.domain.model.City
import java.util.Date

data class WeatherForecastUi(
    val city: City,
    val current: DailyForecastUi,
    val hourly: List<DailyForecastUi>,
    val weekly: List<DailyForecastUi>
)

data class DailyForecastUi(
    val time: Date,
    val minTemp: Double,
    val maxTemp: Double,
    val condition: String,
    val iconUrl: String
)
