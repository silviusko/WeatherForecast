package com.ktt.weatherforecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.ktt.weatherforecast.presentation.navigation.WeatherNavHost
import com.ktt.weatherforecast.presentation.theme.WeatherForecastTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherForecastTheme {
                val navController = rememberNavController()
                WeatherNavHost(navController = navController)
            }
        }
    }
}