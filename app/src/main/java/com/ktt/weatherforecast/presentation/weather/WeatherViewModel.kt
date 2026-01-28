package com.ktt.weatherforecast.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.weatherforecast.domain.repository.CityRepository
import com.ktt.weatherforecast.domain.usecase.GetWeatherForecastUseCase
import com.ktt.weatherforecast.presentation.mapper.WeatherUiMapper
import com.ktt.weatherforecast.presentation.model.WeatherForecastUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadWeather(cityName: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            
            val city = cityRepository.getCityByName(cityName)
            if (city == null) {
                _uiState.value = WeatherUiState.Error("City not found: $cityName")
                return@launch
            }

            getWeatherForecastUseCase(city).fold(
                onSuccess = { forecast ->
                    val uiModel = WeatherUiMapper.map(forecast)
                    _uiState.value = WeatherUiState.Success(uiModel)
                },
                onFailure = { e ->
                    _uiState.value = WeatherUiState.Error(e.localizedMessage ?: "Unknown Error")
                }
            )
        }
    }
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val forecast: WeatherForecastUi) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
