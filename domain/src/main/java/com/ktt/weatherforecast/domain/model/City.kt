package com.ktt.weatherforecast.domain.model

data class City(
    val id: Long,
    val name: String,
    val localName: String? = null,
    val country: String,
    val lat: Double,
    val lon: Double
)
