package com.ktt.weatherforecast.data.di

import com.ktt.weatherforecast.data.repository.CityRepositoryImpl
import com.ktt.weatherforecast.data.repository.WeatherRepositoryImpl
import com.ktt.weatherforecast.domain.repository.CityRepository
import com.ktt.weatherforecast.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCityRepository(
        cityRepositoryImpl: CityRepositoryImpl
    ): CityRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}
