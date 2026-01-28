package com.ktt.weatherforecast.data.mapper

import com.ktt.weatherforecast.data.remote.dto.*
import com.ktt.weatherforecast.domain.model.WeatherForecast
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Date

class WeatherMappersTest {

    @Test
    fun `toWeatherForecast maps CurrentWeatherDto to WeatherForecast correctly`() {
        // Given
        val timestamp = 1678886400L // 2023-03-15 16:00:00 UTC
        val dto = CurrentWeatherDto(
            coord = CoordDto(lat = 10.0, lon = 20.0),
            weather = listOf(WeatherDto(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
            base = "stations",
            main = MainDto(temp = 25.0, feelsLike = 26.0, tempMin = 24.0, tempMax = 26.0, pressure = 1013, humidity = 60),
            visibility = 10000,
            wind = WindDto(speed = 5.0, deg = 180),
            clouds = CloudsDto(all = 0),
            dt = timestamp,
            sys = SysDto(type = 1, id = 123, country = "US", sunrise = 1678860000, sunset = 1678903200),
            timezone = -18000,
            id = 5391959,
            name = "San Francisco",
            cod = 200
        )

        // When
        val result = dto.toWeatherForecast()

        // Then
        assertEquals("San Francisco", result.city.name)
        assertEquals("US", result.city.country)
        assertEquals(24.0, result.current.minTemp, 0.0)
        assertEquals(26.0, result.current.maxTemp, 0.0)
        assertEquals("clear sky", result.current.condition)
        assertEquals("01d", result.current.icon)
        // Date check: 1678886400 * 1000 = 1678886400000
        assertEquals(Date(timestamp * 1000), result.current.time)
    }

    @Test
    fun `toDailyForecast maps CurrentWeatherDto to DailyForecast correctly`() {
         val timestamp = 1678886400L
         val dto = CurrentWeatherDto(
            coord = CoordDto(lat = 10.0, lon = 20.0),
            weather = listOf(WeatherDto(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
            base = "",
            main = MainDto(temp = 25.0, feelsLike = 26.0, tempMin = 24.0, tempMax = 26.0, pressure = 1013, humidity = 60),
            visibility = 10000,
            wind = WindDto(speed = 5.0, deg = 180),
            clouds = CloudsDto(all = 0),
            dt = timestamp,
            sys = SysDto(type = 1, id = 123, country = "US", sunrise = 0, sunset = 0),
            timezone = 0,
            id = 1,
            name = "Test",
            cod = 200
        )

        val result = dto.toDailyForecast()

        assertEquals(24.0, result.minTemp, 0.0)
        assertEquals(26.0, result.maxTemp, 0.0)
        assertEquals("clear sky", result.condition)
        assertEquals(Date(timestamp * 1000), result.time)
    }

    @Test
    fun `toCity maps CurrentWeatherDto to City correctly`() {
        val dto = CurrentWeatherDto(
            coord = CoordDto(lat = 10.0, lon = 20.0),
            weather = emptyList(),
            base = "",
            main = MainDto(temp = 0.0, feelsLike = 0.0, tempMin = 0.0, tempMax = 0.0, pressure = 0, humidity = 0),
            visibility = 0,
            wind = WindDto(speed = 0.0, deg = 0),
            clouds = CloudsDto(all = 0),
            dt = 0,
            sys = SysDto(type = 1, id = 1, country = "UK", sunrise = 0, sunset = 0),
            timezone = 0,
            id = 999,
            name = "London",
            cod = 200
        )

        val result = dto.toCity()

        assertEquals(999, result.id)
        assertEquals("London", result.name)
        assertEquals("UK", result.country)
        assertEquals(10.0, result.lat, 0.0)
        assertEquals(20.0, result.lon, 0.0)
    }

    @Test
    fun `toWeeklyForecast maps FiveDayForecastDto correctly grouping by day`() {
        // Should produce 2 groups
        // Day 1: 10:00 (15°C, 20°C), 13:00 (18°C, 22°C) -> Min 15, Max 22. Icon @ 13:00 (closer to 12)
        // Day 2: 10:00 (10°C, 12°C) -> Min 10, Max 12.

        // Dates need to be consistent. Let's say Day 1 is 2023-01-01.
        val cal = Calendar.getInstance()
        cal.set(2023, Calendar.JANUARY, 1, 10, 0, 0)
        val time1 = cal.timeInMillis / 1000 // 10:00 Day 1

        cal.set(2023, Calendar.JANUARY, 1, 13, 0, 0)
        val time2 = cal.timeInMillis / 1000 // 13:00 Day 1

        cal.set(2023, Calendar.JANUARY, 2, 10, 0, 0)
        val time3 = cal.timeInMillis / 1000 // 10:00 Day 2

        val list = listOf(
            itemDto(time1, 15.0, 20.0, "rain", "10d"),
            itemDto(time2, 18.0, 22.0, "cloudy", "03d"),
            itemDto(time3, 10.0, 12.0, "snow", "13d")
        )

        val dto = FiveDayForecastDto(
            cod = "200",
            message = 0,
            cnt = 3,
            list = list,
            city = CityDto(1, "City", CoordDto(0.0, 0.0), "US", 0, 0, 0, 0)
        )

        val result = dto.toWeeklyForecast()

        assertEquals(2, result.size)

        // Day 1
        val day1 = result[0]
        assertEquals(15.0, day1.minTemp, 0.0)
        assertEquals(22.0, day1.maxTemp, 0.0)
        assertEquals("cloudy", day1.condition) // 13:00 is closer to 12:00 than 10:00
        assertEquals("03d", day1.icon)

        // Day 2
        val day2 = result[1]
        assertEquals(10.0, day2.minTemp, 0.0)
        assertEquals(12.0, day2.maxTemp, 0.0)
        assertEquals("snow", day2.condition)
        assertEquals("13d", day2.icon)
    }

    private fun itemDto(dt: Long, min: Double, max: Double, desc: String, icon: String): ForecastItemDto {
        return ForecastItemDto(
            dt = dt,
            main = MainDto(temp = (min+max)/2, feelsLike = 0.0, tempMin = min, tempMax = max, pressure = 1000, humidity = 50),
            weather = listOf(WeatherDto(id = 800, main = "Main", description = desc, icon = icon)),
            clouds = CloudsDto(0),
            wind = WindDto(0.0, 0),
            visibility = 10000,
            pop = 0.0,
            sys = SysForecastDto("d"),
            dtTxt = ""
        )
    }
}
