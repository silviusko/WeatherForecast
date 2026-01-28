package com.ktt.weatherforecast.presentation.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ktt.weatherforecast.R
import com.ktt.weatherforecast.presentation.model.DailyForecastUi
import com.ktt.weatherforecast.presentation.model.WeatherForecastUi
import com.ktt.weatherforecast.presentation.theme.WeatherDarkBgBottom
import com.ktt.weatherforecast.presentation.theme.WeatherDarkBgTop
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    cityName: String,
    onNavigateBack: () -> Unit,
    onNavigateToCityList: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(cityName) {
        viewModel.loadWeather(cityName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WeatherDarkBgTop, WeatherDarkBgBottom)
                )
            )
    ) {
        when (val state = uiState) {
            is WeatherUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
            is WeatherUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message, color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadWeather(cityName) }) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
            is WeatherUiState.Success -> {
                WeatherContent(
                    forecast = state.forecast,
                    onBackClick = onNavigateBack,
                    onChangeCityClick = onNavigateToCityList
                )
            }
        }
    }
}

@Composable
fun WeatherContent(
    forecast: WeatherForecastUi,
    onBackClick: () -> Unit,
    onChangeCityClick: () -> Unit
) {
    // Current Weather Section + Scrollable Content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings),
                    tint = Color.White
                )
            }
            Text(
                text = forecast.city.localName ?: forecast.city.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.size(48.dp)) 
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main Content Scrollable if needed, but fitting in screen is better.
        // Assuming fit in screen or use LazyColumn for the whole page.
        // For simplicity, we use LazyColumn for the WHOLE page content below Top Bar
        
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                // Current Weather
                AsyncImage(
                    model = forecast.current.iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = forecast.current.condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Text(
                    text = "${forecast.current.maxTemp.toInt()}°",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp), // Larger
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(
                        R.string.forecast_max_min_format, 
                        forecast.current.maxTemp.toInt(), 
                        forecast.current.minTemp.toInt()
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Hourly Forecast Section
                ForecastSectionCard(
                    title = stringResource(R.string.hourly_forecast_title),
                    iconVector = null
                ) {
                   LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(forecast.hourly) { item ->
                            HourlyForecastItem(item)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Weekly Forecast Section
                ForecastSectionCard(
                    title = stringResource(R.string.forecast_weekly_title),
                    iconVector = Icons.Filled.DateRange
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(forecast.weekly) { item ->
                            DailyForecastVerticalItem(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastSectionCard(
    title: String,
    iconVector: androidx.compose.ui.graphics.vector.ImageVector?,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.2f)), // Glass-like
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconVector != null) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun HourlyForecastItem(item: DailyForecastUi) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(60.dp) 
    ) {
        Text(
            text = timeFormat.format(item.time),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            model = item.iconUrl,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = "${item.maxTemp.roundToInt()}°",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DailyForecastVerticalItem(item: DailyForecastUi) {
    val dateFormat = SimpleDateFormat("EEE", Locale.getDefault()) // Mon, Tue...
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(60.dp)
    ) {
        Text(
            text = dateFormat.format(item.time).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            model = item.iconUrl,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = "${item.maxTemp.roundToInt()}°",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
         Text(
            text = "${item.minTemp.roundToInt()}°",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}
