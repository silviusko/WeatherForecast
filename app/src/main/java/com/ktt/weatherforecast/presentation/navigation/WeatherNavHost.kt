package com.ktt.weatherforecast.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ktt.weatherforecast.presentation.city_list.CityListScreen
import com.ktt.weatherforecast.presentation.launch.LaunchScreen
import com.ktt.weatherforecast.presentation.weather.WeatherScreen

object Routes {
    const val LAUNCH = "launch"
    const val CITY_LIST = "city_list"
    const val WEATHER = "weather/{cityName}"
    
    fun weather(cityName: String) = "weather/$cityName"
}

@Composable
fun WeatherNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.LAUNCH) {
        
        composable(Routes.LAUNCH) {
            LaunchScreen(
                onNavigateToCityList = {
                    navController.navigate(Routes.CITY_LIST) {
                        popUpTo(Routes.LAUNCH) { inclusive = true }
                    }
                },
                onNavigateToWeather = { cityName ->
                    navController.navigate(Routes.weather(cityName)) {
                        popUpTo(Routes.LAUNCH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.CITY_LIST) {
            CityListScreen(
                onCitySelected = { cityName ->
                    // Scenario: Selected a city (from Search).
                    // We want to go to Weather and make it the new root (clearing back stack).
                    // Covers:
                    // 1. Launch -> CityList -> Weather (Back exits)
                    // 2. WeatherA -> CityList -> WeatherB (Back exits)
                    navController.navigate(Routes.weather(cityName)) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.WEATHER,
            arguments = listOf(navArgument("cityName") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: return@composable
            WeatherScreen(
                cityName = cityName,
                onNavigateBack = {
                    // This callback matches the Settings Icon click.
                    // We always want to go to City List to allow changing city.
                    // Scenario: Weather -> CityList (Back pops to Weather)
                    navController.navigate(Routes.CITY_LIST)
                },
                onNavigateToCityList = {
                    navController.navigate(Routes.CITY_LIST)
                }
            )
        }
    }
}
