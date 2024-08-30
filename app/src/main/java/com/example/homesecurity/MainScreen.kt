package com.example.homesecurity

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.ui.authenticator.SignedInState

@Composable
fun MainScreen(state: SignedInState) {
    val navController = rememberNavController()
    val context = LocalContext.current

    /*
    LaunchedEffect(Unit) {
        if(state.step == AuthenticatorStep.SignedIn) {
            val selectedPersonId = getSelectedPerson(context)

            if (selectedPersonId == null) {
                navController.navigate(BottomBarScreen.Settings.route)
                Toast.makeText(
                    context,
                    "seleziona un utente",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    */


    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        BottomNavGraph(navController = navController, paddingValues)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Settings,
        BottomBarScreen.Record,
        BottomBarScreen.Home,
        BottomBarScreen.SmartObj,
        BottomBarScreen.Geofencing,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = colorResource(id = R.color.white)) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(screen.icon),
                contentDescription = "Navigation Icon",
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}