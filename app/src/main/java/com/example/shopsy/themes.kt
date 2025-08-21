    package com.example.shopsy.ui.theme

    import androidx.compose.foundation.isSystemInDarkTheme
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.darkColorScheme
    import androidx.compose.material3.lightColorScheme
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.graphics.Color

    private val DarkColors = darkColorScheme(
        primary = Color(0xFF4CAF50),
        onPrimary = Color.White,
        secondary = Color(0xFF388E3C),
        onSecondary = Color.White,
        background = Color(0xFF121212),
        onBackground = Color.White,
        surface = Color(0xFF1E1E1E),
        onSurface = Color.White,
    )

    private val LightColors = lightColorScheme(
        primary = Color(0xFF4CAF50),
        onPrimary = Color.White,
        secondary = Color(0xFF388E3C),
        onSecondary = Color.Black,
        background = Color(0xFFFFFFFF),
        onBackground = Color.Black,
        surface = Color(0xFFF5F5F5),
        onSurface = Color.Black,
    )

    @Composable
    fun ShopsyTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = if (darkTheme) DarkColors else LightColors

        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            content = content
        )
    }

