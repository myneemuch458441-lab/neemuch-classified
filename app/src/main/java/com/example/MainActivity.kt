package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PostAdScreen
import com.example.ui.screens.PropertyDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PropertyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val propertyViewModel: PropertyViewModel = viewModel()
                    MainNavigation(viewModel = propertyViewModel)
                }
            }
        }
    }
}

@Composable
fun MainNavigation(viewModel: PropertyViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
            AppScreen.HOME -> HomeScreen(viewModel = viewModel)
            AppScreen.DETAIL -> PropertyDetailScreen(viewModel = viewModel)
            AppScreen.POST_AD -> PostAdScreen(viewModel = viewModel)
            AppScreen.ADMIN_PANEL -> AdminPanelScreen(viewModel = viewModel)
            AppScreen.FAVORITES -> FavoritesScreen(viewModel = viewModel)
            AppScreen.MY_ADS -> HomeScreen(viewModel = viewModel)
        }
    }
}
