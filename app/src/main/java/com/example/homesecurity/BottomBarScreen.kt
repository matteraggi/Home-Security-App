package com.example.homesecurity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen (
    val route: String,
    val title: String,
    val icon: ImageVector
){
    object Settings: BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
    object Record: BottomBarScreen(
        route = "record",
        title = "Record",
        icon = Icons.Default.Menu
    )
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object SmartObj: BottomBarScreen(
        route = "smartobj",
        title = "SmartObj",
        icon = Icons.Default.MoreVert
    )
    object Routine: BottomBarScreen(
        route = "routine",
        title = "Routine",
        icon = Icons.Default.Refresh
    )

}