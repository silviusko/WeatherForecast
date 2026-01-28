package com.ktt.weatherforecast.data.mapper

import com.ktt.weatherforecast.data.remote.dto.CurrentWeatherDto
import com.ktt.weatherforecast.data.remote.dto.FiveDayForecastDto
import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.model.DailyForecast
import com.ktt.weatherforecast.domain.model.WeatherForecast
import java.util.Calendar
import java.util.Date

fun CurrentWeatherDto.toWeatherForecast(): WeatherForecast {
    val currentDaily = DailyForecast(
        time = Date(this.dt * 1000L), // Convert seconds to milliseconds
        minTemp = this.main.tempMin,
        maxTemp = this.main.tempMax,
        condition = this.weather.firstOrNull()?.description ?: "",
        icon = this.weather.firstOrNull()?.icon ?: ""
    )

    val city = City(
        id = this.id,
        name = this.name,
        country = this.sys.country ?: "",
        lat = this.coord.lat,
        lon = this.coord.lon
    )

    // Current weather endpoint doesn't give weekly forecast, so strictly this mapping is partial
    // Ideally we combine results from both endpoints in the Repository.
    // This function might be better split or used differently.
    return WeatherForecast(
        city = city,
        current = currentDaily,
        weekly = emptyList(), // To be filled by 5-day forecast
        hourly = emptyList()
    )
}

fun CurrentWeatherDto.toDailyForecast(): DailyForecast {
    return DailyForecast(
        time = Date(this.dt * 1000L),
        minTemp = this.main.tempMin,
        maxTemp = this.main.tempMax,
        condition = this.weather.firstOrNull()?.description ?: "",
        icon = this.weather.firstOrNull()?.icon ?: ""
    )
}

fun CurrentWeatherDto.toCity(): City {
    return City(
        id = this.id,
        name = this.name,
        country = this.sys.country ?: "",
        lat = this.coord.lat,
        lon = this.coord.lon
    )
}

fun FiveDayForecastDto.toWeeklyForecast(): List<DailyForecast> {
    // Group by day using Calendar
    val grouped = this.list.groupBy {
        val date = Date(it.dt * 1000L)
        val cal = Calendar.getInstance()
        cal.time = date
        // Create a unique key for the day: YYYY-DDD (Day of year)
        "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.DAY_OF_YEAR)}"
    }

    return grouped.map { (_, items) ->
        val minTemp = items.minOf { it.main.tempMin }
        val maxTemp = items.maxOf { it.main.tempMax }
        
        // Pick the icon/condition from the item close to noon (12:00)
        // or just the first one if not found.
        val representative = items.minByOrNull { 
            val date = Date(it.dt * 1000L)
            val cal = Calendar.getInstance()
            cal.time = date
            Math.abs(12 - cal.get(Calendar.HOUR_OF_DAY)) 
        } ?: items.first()

        // Use the date of the representative item (or the first item) for the daily forecast date
        // Ideally we might want to set the time to 00:00:00 but keeping representative time is also fine.
        // Let's normalize to start of day for cleaner sorting if needed, but simple Date is fine.
        val repDate = Date(representative.dt * 1000L)

        DailyForecast(
            time = repDate, 
            minTemp = minTemp,
            maxTemp = maxTemp,
            condition = representative.weather.firstOrNull()?.description ?: "",
            icon = representative.weather.firstOrNull()?.icon ?: ""
        )
    }.sortedBy { it.time }
}

fun FiveDayForecastDto.toHourlyForecast(): List<DailyForecast> {
    // Take the first 8 items (approx 24 hours, as data is every 3 hours)
    // or take all, depending on UI needs. Let's take first 8-10.
    return this.list.take(12).map { item ->
        DailyForecast(
            time = Date(item.dt * 1000L),
            minTemp = item.main.tempMin,
            maxTemp = item.main.tempMax,
            condition = item.weather.firstOrNull()?.description ?: "",
            icon = item.weather.firstOrNull()?.icon ?: ""
        )
    }
}
