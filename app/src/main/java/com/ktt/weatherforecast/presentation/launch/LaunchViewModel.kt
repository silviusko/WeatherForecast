package com.ktt.weatherforecast.presentation.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.weatherforecast.domain.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LaunchUiState>(LaunchUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        checkSavedCity()
    }

    private fun checkSavedCity() {
        viewModelScope.launch {
            // First ensure DB is populated
            cityRepository.ensureCityDataInitialized()
            
            val savedCity = cityRepository.getSavedCity()
            if (savedCity != null) {
                _uiState.value = LaunchUiState.NavigateToWeather(savedCity.name)
            } else {
                _uiState.value = LaunchUiState.NavigateToCityList
            }
        }
    }
}

sealed class LaunchUiState {
    object Loading : LaunchUiState()
    data class NavigateToWeather(val cityName: String) : LaunchUiState()
    object NavigateToCityList : LaunchUiState()
}
