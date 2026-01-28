package com.ktt.weatherforecast.data.datasource

import android.content.Context
import android.content.res.AssetManager
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.ktt.weatherforecast.data.local.AppDatabase
import com.ktt.weatherforecast.data.local.dao.CityDao
import com.ktt.weatherforecast.data.local.entity.CityEntity
import com.ktt.weatherforecast.domain.model.City
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

@OptIn(ExperimentalCoroutinesApi::class)
class LocalCityDataSourceTest {

    private lateinit var dataSource: LocalCityDataSource
    private val cityDao: CityDao = mockk(relaxed = true)
    private val db: AppDatabase = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)
    private val assetManager: AssetManager = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic("androidx.room.RoomDatabaseKt")
        val transactionLambda = slot<suspend () -> R>()
        coEvery { db.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        every { context.assets } returns assetManager
        dataSource = LocalCityDataSourceImpl(cityDao, db, context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `searchCities maps entities to domain models`() = runTest {
        val query = "London"
        val entities = listOf(
            CityEntity(1, "London", "London", "UK", 51.5, -0.1, false)
        )
        every { cityDao.searchCities(query) } returns flowOf(entities)

        dataSource.searchCities(query).collect { cities ->
            assertEquals(1, cities.size)
            assertEquals("London", cities[0].name)
        }
    }

    @Test
    fun `saveCity executes transaction to clear and insert`() = runTest {
        val city = City(1, "London", "London", "UK", 51.5, -0.1)
        
        dataSource.saveCity(city)
        
        coVerify { cityDao.clearSavedCity() }
        coVerify { cityDao.insertCity(any()) }
    }
    
    @Test
    fun `ensureCityDataInitialized parses json and inserts when empty`() = runTest {
        coEvery { cityDao.getCityCount() } returns 0
        
        val json = """
            [
                {
                    "id": 1,
                    "name": "Test City",
                    "country": "TC",
                    "coord": {
                        "lon": 10.0,
                        "lat": 20.0
                    }
                }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())
        every { assetManager.open("city.list.min.json") } returns inputStream
        
        dataSource.ensureCityDataInitialized()
        
        coVerify { cityDao.insertCities(any()) }
    }
    
    @Test
    fun `ensureCityDataInitialized does nothing if data exists`() = runTest {
        coEvery { cityDao.getCityCount() } returns 100
        
        dataSource.ensureCityDataInitialized()
        
        coVerify(exactly = 0) { assetManager.open(any()) }
        coVerify(exactly = 0) { cityDao.insertCities(any()) }
    }
}

// Helper for generic slot
private inline fun <reified T : Any> slot() = io.mockk.slot<T>()
// Helper for return type (compiler workaround)
private typealias R = Any?
