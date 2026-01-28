package com.ktt.weatherforecast.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class DtoTest {

    @Test
    fun `test CurrentWeatherDto coverage`() {
        val dto = CurrentWeatherDto(
            coord = CoordDto(10.0, 20.0),
            weather = listOf(WeatherDto(800, "Clear", "desc", "icon")),
            base = "stations",
            main = MainDto(10.0, 11.0, 9.0, 12.0, 1000, 50),
            visibility = 10000,
            wind = WindDto(5.0, 180),
            clouds = CloudsDto(0),
            dt = 1234567890L,
            sys = SysDto(1, 100, "US", 0, 0),
            timezone = 3600,
            id = 123,
            name = "Test City",
            cod = 200
        )
        
        assertNotNull(dto)
        assertEquals("stations", dto.base)
        assertEquals(10000, dto.visibility)
        assertEquals(3600, dto.timezone)
        assertEquals(200, dto.cod)
        assertEquals(1234567890L, dto.dt)
        assertEquals("Test City", dto.name)
        assertEquals(123L, dto.id)
        
        // Exercise sub-dtos
        assertEquals(10.0, dto.coord.lon, 0.0)
        assertEquals(20.0, dto.coord.lat, 0.0)
        
        assertEquals(800, dto.weather[0].id)
        assertEquals("Clear", dto.weather[0].main)
        assertEquals("desc", dto.weather[0].description)
        assertEquals("icon", dto.weather[0].icon)
        
        assertEquals(10.0, dto.main.temp, 0.0)
        assertEquals(11.0, dto.main.feelsLike, 0.0)
        assertEquals(1000, dto.main.pressure)
        assertEquals(50, dto.main.humidity)
        
        assertEquals(5.0, dto.wind.speed, 0.0)
        assertEquals(180, dto.wind.deg)
        
        assertEquals(0, dto.clouds.all)
        
        assertEquals(1, dto.sys.type)
        assertEquals(100, dto.sys.id)
        assertEquals("US", dto.sys.country)
    }

    @Test
    fun `test FiveDayForecastDto coverage`() {
        val item = ForecastItemDto(
            dt = 123456L,
            main = MainDto(1.0, 1.0, 1.0, 1.0, 1, 1),
            weather = listOf(),
            clouds = CloudsDto(1),
            wind = WindDto(1.0, 1),
            visibility = 1000,
            pop = 0.5,
            sys = SysForecastDto("d"),
            dtTxt = "2023-01-01"
        )
        
        val cityDto = CityDto(
            id = 1,
            name = "City",
            coord = CoordDto(0.0, 0.0),
            country = "US",
            population = 1000,
            timezone = 0,
            sunrise = 0,
            sunset = 0
        )
        
        val dto = FiveDayForecastDto(
            cod = "200",
            message = 0,
            cnt = 1,
            list = listOf(item),
            city = cityDto
        )
        
        assertEquals("200", dto.cod)
        assertEquals(0, dto.message)
        assertEquals(1, dto.cnt)
        assertEquals(1, dto.list.size)
        assertEquals("City", dto.city.name)
        
        assertEquals(123456L, item.dt)
        assertEquals(1000, item.visibility)
        assertEquals(0.5, item.pop, 0.0)
        assertEquals("d", item.sys.pod)
        assertEquals("2023-01-01", item.dtTxt)
        
        assertEquals(1000, cityDto.population)
        assertEquals(0, cityDto.timezone)
        assertEquals(0L, cityDto.sunrise)
        assertEquals(0L, cityDto.sunset)
    }
}
