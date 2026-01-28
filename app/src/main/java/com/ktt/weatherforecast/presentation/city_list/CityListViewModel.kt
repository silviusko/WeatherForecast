package com.ktt.weatherforecast.presentation.city_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.weatherforecast.domain.model.City
import com.ktt.weatherforecast.domain.repository.CityRepository
import com.ktt.weatherforecast.domain.usecase.SearchCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
class CityListViewModel @Inject constructor(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities = _cities.asStateFlow()

    init {
        searchQuery
            .debounce(500) // Debounce search 0.5s to save API usage
            .flatMapLatest { query ->
                if (query.length < 2) {
                    flowOf(emptyList())
                } else {
                    searchCitiesUseCase(query)
                }
            }
            .onEach { _cities.value = it }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCitySelected(city: City) {
        viewModelScope.launch {
            cityRepository.saveCity(city)
        }
    }
}
