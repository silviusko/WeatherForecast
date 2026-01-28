package com.ktt.weatherforecast.di

import com.ktt.weatherforecast.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Provides
    @Singleton
    @Named("open_weather_api_key")
    fun provideOpenWeatherApiKey(): String {
        return BuildConfig.OPEN_WEATHER_API_KEY
    }
}
