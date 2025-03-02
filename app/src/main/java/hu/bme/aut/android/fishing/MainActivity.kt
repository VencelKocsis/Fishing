package hu.bme.aut.android.fishing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.fishing.navigation.AppNavigation
import hu.bme.aut.android.fishing.ui.theme.FishingTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FishingTheme {
                AppNavigation()
            }
        }
    }
}