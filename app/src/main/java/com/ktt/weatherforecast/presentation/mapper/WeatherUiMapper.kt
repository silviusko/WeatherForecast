package com.ktt.weatherforecast.presentation.mapper

import com.ktt.weatherforecast.domain.model.DailyForecast
import com.ktt.weatherforecast.domain.model.WeatherForecast
import com.ktt.weatherforecast.presentation.model.DailyForecastUi
import com.ktt.weatherforecast.presentation.model.WeatherForecastUi

object WeatherUiMapper {
    
    fun map(domain: WeatherForecast): WeatherForecastUi {
        return WeatherForecastUi(
            city = domain.city,
            current = mapItem(domain.current, isLarge = true),
            hourly = domain.hourly.map { mapItem(it, isLarge = false) },
            weekly = domain.weekly.map { mapItem(it, isLarge = false) }
        )
    }

    private fun mapItem(item: DailyForecast, isLarge: Boolean): DailyForecastUi {
        val iconSize = if (isLarge) "@4x" else "@2x"
        return DailyForecastUi(
            time = item.time,
            minTemp = item.minTemp,
            maxTemp = item.maxTemp,
            condition = item.condition,
            iconUrl = "https://openweathermap.org/img/wn/${item.icon}$iconSize.png"
        )
    }
}
