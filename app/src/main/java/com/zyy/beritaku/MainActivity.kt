package com.zyy.beritaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zyy.beritaku.ui.screens.HomeScreen
import com.zyy.beritaku.ui.theme.BeritakuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeritakuTheme {
                HomeScreen()
            }
        }
    }
}
