package com.example.gearhubmobile.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBF3434),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3A1A1A),
    onPrimaryContainer = Color(0xFFFFDAD6),

    secondary = Color(0xFFE63E3E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4B2323),
    onSecondaryContainer = Color(0xFFFFDAD6),

    tertiary = Color(0xFFD93B3B),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF3A2323),
    onTertiaryContainer = Color(0xFFFFDAD6),

    background = Color(0xFF181818),
    onBackground = Color(0xFFF5F5F5),

    surface = Color(0xFF232323),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF3A3A3A),
    onSurfaceVariant = Color(0xFFE0E0E0),

    surfaceTint = Color(0xFF9F2D2D),
    inverseSurface = Color(0xFFF5F5F5),
    inverseOnSurface = Color(0xFF232323),

    outline = Color(0xFF8A8A8A),
    outlineVariant = Color(0xFF444444),

    scrim = Color(0x66000000),

    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFFFDAD6)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple80,
    secondary = PurpleGrey40,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun GearHubMobileTheme(
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}