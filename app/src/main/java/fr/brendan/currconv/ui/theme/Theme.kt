package fr.brendan.currconv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    surfaceContainer = UltraDarkGray,
    surfaceVariant = UltraDarkGray,
    onSurfaceVariant = White,

    primary = Orange,
    onPrimary = White,
    secondary = LightOrange,
    onSecondary = White
)

private val LightColorScheme = lightColorScheme(
    surfaceContainer = UltraLightGray,
    surfaceVariant = White,
    onSurfaceVariant = UltraDarkGray,

    primary = Orange,
    onPrimary = White,
    secondary = LightOrange,
    onSecondary = White
)

@Composable
fun CurrConvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}