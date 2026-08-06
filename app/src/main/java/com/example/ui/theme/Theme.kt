package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val KonsoDarkColorScheme = darkColorScheme(
    primary = SleekPrimary,
    onPrimary = SleekOnPrimary,
    primaryContainer = SleekPrimaryContainer,
    onPrimaryContainer = SleekOnPrimaryContainer,
    secondary = SleekSecondary,
    onSecondary = SleekOnSecondary,
    secondaryContainer = SleekSecondaryContainer,
    onSecondaryContainer = SleekOnSecondaryContainer,
    tertiary = SleekTertiary,
    onTertiary = SleekOnTertiary,
    tertiaryContainer = SleekTertiaryContainer,
    onTertiaryContainer = SleekOnTertiaryContainer,
    background = SleekBackgroundDark,
    onBackground = SleekOnBackgroundDark,
    surface = SleekSurfaceDark,
    onSurface = SleekOnSurfaceDark,
    surfaceVariant = SleekSurfaceVariantDark,
    onSurfaceVariant = SleekOnBackgroundDark,
    error = SleekError
)

private val KonsoLightColorScheme = lightColorScheme(
    primary = SleekPrimary,
    onPrimary = SleekOnPrimary,
    primaryContainer = SleekPrimaryContainer,
    onPrimaryContainer = SleekOnPrimaryContainer,
    secondary = SleekSecondary,
    onSecondary = SleekOnSecondary,
    secondaryContainer = SleekSecondaryContainer,
    onSecondaryContainer = SleekOnSecondaryContainer,
    tertiary = SleekTertiary,
    onTertiary = SleekOnTertiary,
    tertiaryContainer = SleekTertiaryContainer,
    onTertiaryContainer = SleekOnTertiaryContainer,
    background = SleekBackgroundLight,
    onBackground = SleekOnBackgroundLight,
    surface = SleekSurfaceLight,
    onSurface = SleekOnSurfaceLight,
    surfaceVariant = SleekSurfaceVariantLight,
    onSurfaceVariant = SleekOnSurfaceVariantLight,
    error = SleekError
)

@Composable
fun KonsoMediaTheme(
    darkTheme: Boolean = false, // Default to Sleek Interface light canvas (#F7F9FC)
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> KonsoDarkColorScheme
        else -> KonsoLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
