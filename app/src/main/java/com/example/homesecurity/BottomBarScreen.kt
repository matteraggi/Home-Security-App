package com.example.homesecurity


sealed class BottomBarScreen (
    val route: String,
    val title: String,
    val icon: Int
){
    object Settings: BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = R.drawable.ic_settings_black_24dp
    )
    object Record: BottomBarScreen(
        route = "record",
        title = "Records",
        icon = R.drawable.ic_record_black_24dp
    )
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = R.drawable.ic_home_black_24dp
    )
    object SmartObj: BottomBarScreen(
        route = "smartobj",
        title = "Objects",
        icon = R.drawable.ic_smartobj_black_24dp
    )
    object Geofencing: BottomBarScreen(
        route = "authentication",
        title = "Auth",
        icon = R.drawable.ic_routine_black_24dp
    )
}