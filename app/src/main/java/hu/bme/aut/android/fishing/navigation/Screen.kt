package hu.bme.aut.android.fishing.navigation

sealed class Screen(val route: String) {
    object Authentication: Screen("authentication")
    object ListCatches: Screen("catches")
}