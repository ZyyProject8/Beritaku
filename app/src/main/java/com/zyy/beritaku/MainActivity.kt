package com.zyy.beritaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.zyy.beritaku.ui.screens.HomeScreen
import com.zyy.beritaku.ui.screens.SplashScreen
import com.zyy.beritaku.ui.theme.BeritakuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Mode Gelap (Night) dan Terang (Not Night) otomatis mengikuti setelan bawaan HP
            val isSystemDark = isSystemInDarkTheme()
            // Pengaturan Multi Bahasa (Indonesia & English) yang diatur di tab Pengaturan
            var isEnglish by rememberSaveable { mutableStateOf(false) }
            // State SplashScreen
            var showSplash by rememberSaveable { mutableStateOf(true) }

            BeritakuTheme(darkTheme = isSystemDark) {
                if (showSplash) {
                    SplashScreen(onSplashFinished = { showSplash = false })
                } else {
                    HomeScreen(
                        isDarkMode = isSystemDark,
                        isEnglish = isEnglish,
                        onToggleLanguage = { isEnglish = it }
                    )
                }
            }
        }
    }
}
