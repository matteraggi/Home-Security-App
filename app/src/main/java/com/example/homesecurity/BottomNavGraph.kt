package com.example.homesecurity

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homesecurity.ui.home.HomeScreen
import com.example.homesecurity.ui.record.RecordScreen
import com.example.homesecurity.ui.routine.RoutineScreen
import com.example.homesecurity.ui.settings.SettingsScreen
import com.example.homesecurity.ui.singlerecord.SingleRecordScreen
import com.example.homesecurity.ui.smartobj.SmartObjScreen

@Composable
fun BottomNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen()
        }
        composable(route = BottomBarScreen.Record.route) {
            RecordScreen(navController)
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = BottomBarScreen.SmartObj.route) {
            SmartObjScreen()
        }
        composable(route = BottomBarScreen.Routine.route) {
            RoutineScreen()
        }
        composable(route = NotBottomBarPages.SingleRecord.route + "/{timestamp}", arguments = listOf(navArgument("timestamp"){
            type = NavType.StringType
            defaultValue = "0"
            nullable = true
        })) {entry ->
            SingleRecordScreen(timestamp = entry.arguments?.getString("timestamp"))
        }
    }
}