package hu.bme.aut.android.fishing.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.fishing.feature.auth.AuthenticationScreen
import hu.bme.aut.android.fishing.feature.catches.local_catches.LocalCatchesScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Authentication.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Authentication.route) {
            AuthenticationScreen()
        }
        composable(Screen.LocalCatches.route) {
            LocalCatchesScreen()
        }
    }
}