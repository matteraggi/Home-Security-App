package com.example.homesecurity.ui.wifilist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.R

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun WifiListScreen(navController: NavController) {
    val wifiViewModel: WifiListViewModel = viewModel()
    val wifiListState = wifiViewModel.wifiList.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedSsid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { wifiViewModel.startScan() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Scan WiFi Networks")
        }
        LazyColumn {
            items(wifiListState.value) { wifi ->
                if(wifi.ssid.length > 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                selectedSsid = wifi.ssid
                                showDialog = true
                            }
                    ) {
                        Text(
                            text = "SSID: ${wifi.ssid}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        ArrowButton(wifiViewModel, navController)
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Connect to $selectedSsid") },
                text = {
                    Column {
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            wifiViewModel.connectToWifi(selectedSsid, password)
                            showDialog = false
                        }
                    ) {
                        Text("Connect")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ArrowButton(viewModel: WifiListViewModel, navController: NavController) {
    val arrowButtonEnabled by viewModel.arrowButtonEnabled.collectAsState()

    if (arrowButtonEnabled) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    navController.navigate(NotBottomBarPages.ConnectionWebView.route)
                },
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(
                onClick = {
                    navController.navigate(NotBottomBarPages.ConnectionWebView.route)
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = "Navigate to WebView"
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(enabled = false) { /* Non fa nulla quando è disabilitato */ },
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(
                onClick = {
                    // Non fa nulla quando è disabilitato
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = "Navigate to WebView",
                    tint = Color.Gray
                )
            }
        }
    }
}