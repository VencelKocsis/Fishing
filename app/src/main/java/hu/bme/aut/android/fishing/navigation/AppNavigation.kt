package hu.bme.aut.android.fishing.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.fishing.navigation.items.BottomNavigationItems

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                BottomNavigationItems.items.forEach { navItem ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.title
                            )
                        },
                        label = {
                            Text(text = navItem.title)
                        },
                        selected = currentDestination?.route == navItem.route,
                        onClick = {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = Screen.Authentication.route,
            modifier = Modifier.padding(innerPadding)
        )
    }
}