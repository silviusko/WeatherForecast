package com.ktt.weatherforecast.domain.usecase

import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(query: String): Flow<List<City>> {
        return repository.searchCities(query)
    }
}
