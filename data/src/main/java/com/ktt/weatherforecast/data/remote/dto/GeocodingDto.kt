package com.ktt.weatherforecast.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeocodingDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("country")
    val country: String,
    @SerializedName("local_names")
    val localNames: Map<String, String>?,
    @SerializedName("state")
    val state: String?
)
