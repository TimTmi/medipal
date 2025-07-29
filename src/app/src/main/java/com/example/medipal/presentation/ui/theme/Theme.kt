package com.example.medipal.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color // Đảm bảo đã import Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat




// Bảng màu cho Chế độ Tối (Dark Theme) - Phù hợp với thiết kế của bạn
private val DarkColorScheme = darkColorScheme(
    primary = DarkGreen,
    secondary = LightGreen,
    tertiary = MediumGreen,
    background = BackgroundGreen,
    surface = DarkGreen,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

// Bảng màu cho Chế độ Sáng (Light Theme)
private val LightColorScheme = lightColorScheme(
    primary = DarkGreen,
    secondary = LightGreen,
    tertiary = MediumGreen,
    background = Color(0xFFF5F5F5), // Màu nền sáng
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun MediPalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Mặc định dùng theme của hệ thống
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
