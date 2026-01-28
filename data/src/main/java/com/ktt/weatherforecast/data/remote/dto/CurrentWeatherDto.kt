package com.ktt.weatherforecast.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CurrentWeatherDto(
    @SerializedName("coord") val coord: CoordDto,
    @SerializedName("weather") val weather: List<WeatherDto>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val main: MainDto,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("clouds") val clouds: CloudsDto,
    @SerializedName("dt") val dt: Long,
    @SerializedName("sys") val sys: SysDto,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Int
)

data class SysDto(
    @SerializedName("type") val type: Int?, // Can be null in some responses
    @SerializedName("id") val id: Int?,
    @SerializedName("country") val country: String?,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)
