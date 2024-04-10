package com.example.homesecurity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.homesecurity.ui.home.HomeScreen
import com.example.homesecurity.ui.record.RecordScreen
import com.example.homesecurity.ui.routine.RoutineScreen
import com.example.homesecurity.ui.settings.SettingsScreen
import com.example.homesecurity.ui.smartobj.SmartObjScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen()
        }
        composable(route = BottomBarScreen.Record.route) {
            RecordScreen()
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarScreen.SmartObj.route) {
            SmartObjScreen()
        }
        composable(route = BottomBarScreen.Routine.route) {
            RoutineScreen()
        }
    }
}