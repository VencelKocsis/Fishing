package hu.bme.aut.android.fishing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import hu.bme.aut.android.fishing.navigation.AppNavigation
import hu.bme.aut.android.fishing.ui.theme.FishingTheme

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