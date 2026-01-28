package com.ktt.weatherforecast.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = WeatherAccent,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = WeatherDarkBgTop, // Gradient usually handled in UI modifiers
    surface = WeatherSurface,
    onPrimary = WeatherAccentText,
    onSecondary = WeatherAccentText,
    onTertiary = WeatherAccentText,
    onBackground = WeatherOnSurface,
    onSurface = WeatherOnSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
    // Keep default light theme just in case, but app is dark themed by design.
)

@Composable
fun WeatherForecastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic color to stick to design
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> DarkColorScheme // Force dark theme for this design? Or respect system? Design implies Dark. Let's default to dark even in light mode if we want to match design strictly. But let's respect darkTheme param if user passes it, but default DarkColorScheme for both for now to ensure consistency with design? No, let's stick to DarkColorScheme mostly.
    }
    
    // Force Dark Theme for now as per design "Dark Theme"
    val finalColorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = finalColorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false // Dark bg -> Light icons
        }
    }

    MaterialTheme(
        colorScheme = finalColorScheme,
        typography = Typography,
        content = content
    )
}
