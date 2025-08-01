package com.jamie.kitobox

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jamie.kitobox.navigation.AppNavigation
import com.jamie.kitobox.ui.theme.KitoboxTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : FragmentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()

            val theme by mainViewModel.currentTheme.collectAsStateWithLifecycle()

            KitoboxTheme(appTheme = theme) {
                AppNavigation(startDestination = startDestination)
            }
        }
    }
}
