package com.ktt.weatherforecast.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ktt.weatherforecast.data.local.dao.CityDao
import com.ktt.weatherforecast.data.local.entity.CityEntity

@Database(entities = [CityEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    
    companion object {
        const val DATABASE_NAME = "weather_db"
    }
}
