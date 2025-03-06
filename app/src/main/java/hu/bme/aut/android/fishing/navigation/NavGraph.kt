package hu.bme.aut.android.fishing.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.aut.android.fishing.feature.auth.AuthenticationScreen
import hu.bme.aut.android.fishing.feature.catches.check_catch.CheckCatchScreen
import hu.bme.aut.android.fishing.feature.catches.create_catch.AddCatchScreen
import hu.bme.aut.android.fishing.feature.catches.list_catches.ListCatchesScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
        composable(Screen.ListCatches.route) {
            ListCatchesScreen(
                onListItemClick = {
                    navController.navigate(Screen.CheckCatch.passId(it))
                },
                onFabClick = {
                    navController.navigate(Screen.AddCatch.route)
                }
            )
        }
        composable(Screen.AddCatch.route) {
            AddCatchScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.ListCatches.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.ListCatches.route)
                }
            )
        }
        composable(
            route = Screen.CheckCatch.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            CheckCatchScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.ListCatches.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.ListCatches.route)
                }
            )
        }
    }
}