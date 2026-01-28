package com.ktt.weatherforecast.presentation.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ktt.weatherforecast.R
import com.ktt.weatherforecast.presentation.theme.WeatherDarkBgTop

@Composable
fun LaunchScreen(
    viewModel: LaunchViewModel = hiltViewModel(),
    onNavigateToCityList: () -> Unit,
    onNavigateToWeather: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state) {
        when (val s = state) {
            is LaunchUiState.NavigateToCityList -> onNavigateToCityList()
            is LaunchUiState.NavigateToWeather -> onNavigateToWeather(s.cityName)
            LaunchUiState.Loading -> Unit
        }
    }

    // Simple Loading UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherDarkBgTop), // Match theme bg
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.loading_data_wait),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
