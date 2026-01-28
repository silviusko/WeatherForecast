package com.ktt.weatherforecast.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ktt.weatherforecast.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    // Search by name (case insensitive simple search)
    @Query("SELECT * FROM city WHERE name LIKE :query || '%' LIMIT 20")
    fun searchCities(query: String): Flow<List<CityEntity>>

    @Query("SELECT * FROM city WHERE name = :name LIMIT 1")
    suspend fun getCityByName(name: String): CityEntity?

    @Query("SELECT * FROM city WHERE isSaved = 1 LIMIT 1")
    suspend fun getSavedCity(): CityEntity?

    @Query("UPDATE city SET isSaved = 0")
    suspend fun clearSavedCity()
    
    // Use transaction in Repository: clearSavedCity() then insertCity(city.copy(isSaved=true))
    
    @Query("SELECT COUNT(*) FROM city")
    suspend fun getCityCount(): Int
}
