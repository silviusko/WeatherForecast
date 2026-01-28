package com.ktt.weatherforecast.data.di

import android.content.Context
import androidx.room.Room
import com.ktt.weatherforecast.data.local.AppDatabase
import com.ktt.weatherforecast.data.local.dao.CityDao
import com.ktt.weatherforecast.data.remote.WeatherApiService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideCityDao(db: AppDatabase): CityDao {
        return db.cityDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocalCityDataSource(
        cityDao: CityDao,
        db: AppDatabase,
        @ApplicationContext context: Context
    ): com.ktt.weatherforecast.data.datasource.LocalCityDataSource {
        return com.ktt.weatherforecast.data.datasource.LocalCityDataSourceImpl(cityDao, db, context)
    }

    @Provides
    @Singleton
    fun provideRemoteCityDataSource(
        apiService: WeatherApiService,
        @javax.inject.Named("open_weather_api_key") apiKey: String
    ): com.ktt.weatherforecast.data.datasource.RemoteCityDataSource {
        return com.ktt.weatherforecast.data.datasource.RemoteCityDataSourceImpl(apiService, apiKey)
    }
}
