package com.ktt.weatherforecast.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val localName: String? = null,
    val country: String,
    val lat: Double,
    val lon: Double,
    val isSaved: Boolean = false // Helper to mark the "current/selected" city if we want to store it here
)

// Extension to map Entity to Domain
fun CityEntity.toCity(): com.ktt.weatherforecast.domain.model.City {
    return com.ktt.weatherforecast.domain.model.City(
        id = id,
        name = name,
        localName = localName,
        country = country,
        lat = lat,
        lon = lon
    )
}

// Extension to map Domain to Entity
fun com.ktt.weatherforecast.domain.model.City.toEntity(isSaved: Boolean = false): CityEntity {
    return CityEntity(
        id = id,
        name = name,
        localName = localName,
        country = country,
        lat = lat,
        lon = lon,
        isSaved = isSaved
    )
}
