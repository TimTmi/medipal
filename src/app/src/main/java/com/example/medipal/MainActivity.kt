package com.example.medipal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.medipal.presentation.ui.screens.MainScreen
import com.example.medipal.presentation.ui.theme.MediPalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kích hoạt chế độ edge-to-edge để giao diện vẽ ra sau các thanh hệ thống
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MediPalTheme {
                // MainScreen sẽ quản lý tất cả Scaffold và Navigation.
                MainScreen()
            }
        }
    }
}