package hu.bme.aut.android.fishing.navigation.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterFrames
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import hu.bme.aut.android.fishing.navigation.Screen

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
    data object ListCatchesItem: BottomNavigationItems(
        title = "Fogásaim",
        route = Screen.ListCatches.route,
        icon = Icons.Default.FilterFrames
    )

    companion object {
        val items = listOf(
            ProfileItem,
            ListCatchesItem
        )
    }
}