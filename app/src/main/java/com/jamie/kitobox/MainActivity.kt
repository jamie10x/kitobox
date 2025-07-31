package com.jamie.kitobox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jamie.kitobox.navigation.AppNavigation
import com.jamie.kitobox.ui.theme.KitoboxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitoboxTheme {
                AppNavigation()
            }
        }
    }
}
