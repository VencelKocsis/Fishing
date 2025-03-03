package hu.bme.aut.android.fishing.navigation

sealed class Screen(val route: String) {
    object Authentication: Screen("authentication")
    object Profile: Screen("profile")
    object LocalCatches: Screen("localCatches")
    object GlobalCatches: Screen("globalCatches")

}