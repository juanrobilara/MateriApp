package com.jurobil.materiapp.ui.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val TealPrimary = Color(0xFF00897B)
val TealPrimaryDark = Color(0xFF00695C)
val TealSecondary = Color(0xFF4DB6AC)
val TealBackgroundLight = Color(0xFFF1F8F9)
val TealSurfaceLight = Color(0xFFFFFFFF)
val TealSurfaceVariantLight = Color(0xFFE0F2F1)
val TealTextDark = Color(0xFFE0E0E0)
val TealTextLight = Color(0xFF333333)
val TealLight = Color(0xFF4DB6AC)
val TealDark = Color(0xFF00695C)
val CardLightBackground = Color(0xFFE0F2F1)
val CardDarkBackground = Color(0xFF263238)


private val DarkColorScheme = darkColorScheme(
    primary = TealPrimary,
    secondary = TealSecondary,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF263238),
    onPrimary = Color.White,
    onBackground = TealTextDark,
    onSurface = TealTextDark
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    secondary = TealSecondary,
    background = TealBackgroundLight,
    surface = TealSurfaceLight,
    surfaceVariant = TealSurfaceVariantLight,
    onPrimary = Color.White,
    onBackground = TealTextLight,
    onSurface = TealTextLight
)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */


@Composable
fun MateriAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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