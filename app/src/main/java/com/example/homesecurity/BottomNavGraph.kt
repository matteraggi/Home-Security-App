package com.example.homesecurity

import HomeScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homesecurity.ui.connectionwebview.ConnectionWebViewScreen
import com.example.homesecurity.ui.createuser.CreateUserScreen
import com.example.homesecurity.ui.record.RecordScreen
import com.example.homesecurity.ui.routine.RoutineScreen
import com.example.homesecurity.ui.settings.SettingsScreen
import com.example.homesecurity.ui.singlerecord.SingleRecordScreen
import com.example.homesecurity.ui.smartobj.SmartObjScreen
import com.example.homesecurity.ui.wifilist.WifiListScreen


//controllare se è ok questa linea
@RequiresApi(Build.VERSION_CODES.Q)
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
        composable(
            route = NotBottomBarPages.SingleRecord.route + "/{timestamp}",
            arguments = listOf(navArgument("timestamp") {
                type = NavType.StringType
                defaultValue = "0"
                nullable = true
            })
        ) { entry ->
            SingleRecordScreen(timestamp = entry.arguments?.getString("timestamp"))
        }
        composable(route = NotBottomBarPages.WifiList.route) {
            WifiListScreen(navController)
        }
        composable(route = NotBottomBarPages.ConnectionWebView.route) {
            ConnectionWebViewScreen(navController)
        }
        composable(route = NotBottomBarPages.CreateUser.route) {
            CreateUserScreen(navController)
        }
    }
}