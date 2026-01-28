package com.ktt.weatherforecast.data.local.entity

import com.ktt.weatherforecast.domain.model.City
import org.junit.Assert.assertEquals
import org.junit.Test

class CityEntityTest {

    @Test
    fun `toCity maps Entity to Domain Model correctly`() {
        // Given
        val entity = CityEntity(1, "London", null, "UK", 51.5, -0.1, true)

        // When
        val city = entity.toCity()

        // Then
        assertEquals(1, city.id)
        assertEquals("London", city.name)
        assertEquals("UK", city.country)
        assertEquals(51.5, city.lat, 0.0)
        assertEquals(-0.1, city.lon, 0.0)
    }

    @Test
    fun `toEntity maps Domain Model to Entity correctly`() {
        // Given
        val city = City(1, "London", null, "UK", 51.5, -0.1)

        // When
        val entity = city.toEntity(isSaved = true)

        // Then
        assertEquals(1, entity.id)
        assertEquals("London", entity.name)
        assertEquals("UK", entity.country)
        assertEquals(51.5, entity.lat, 0.0)
        assertEquals(-0.1, entity.lon, 0.0)
        assertEquals(true, entity.isSaved)
    }

    @Test
    fun `toEntity default isSaved is false`() {
        // Given
        val city = City(1, "London", null, "UK", 51.5, -0.1)

        // When
        val entity = city.toEntity()

        // Then
        assertEquals(false, entity.isSaved)
    }
}
