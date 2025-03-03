package hu.bme.aut.android.fishing.navigation.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import hu.bme.aut.android.fishing.navigation.Screen
import hu.bme.aut.android.fishing.ui.icons.Trophy

sealed class BottomNavigationItems(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    data object ProfileItem : BottomNavigationItems(
        title = "Profil",
        route = Screen.Profile.route,
        icon = Icons.Default.Person
    )
    data object LocalCatchesItem: BottomNavigationItems(
        title = "Fogásaim",
        route = Screen.LocalCatches.route,
        icon = Icons.Default.Home
    )
    data object GlobalCatchesItem: BottomNavigationItems(
        title = "Összes",
        route = Screen.GlobalCatches.route,
        icon = Trophy
    )

    companion object {
        val items = listOf(
            ProfileItem,
            LocalCatchesItem,
            GlobalCatchesItem
        )
    }
}