package com.example.homesecurity.ui.settings

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homesecurity.R
import com.google.android.gms.location.LocationServices

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel()

    var lastLat by remember { mutableStateOf(0.0) }
    var lastLon by remember { mutableStateOf(0.0) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        settingsViewModel.startLocationUpdates(fusedLocationClient)
    }

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "Vuoi impostare una nuova posizione per la tua casa?"
                )
                Text(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    text = "Quando ti allontanerai dalla posizione, verrai automaticamente rilevato come fuori dalla casa,  e viceversa"
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue_medium)),
                    onClick = {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Request permissions if they are not granted
                        ActivityCompat.requestPermissions(
                            (context as Activity),
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            REQUEST_LOCATION_PERMISSION
                        )
                        return@Button
                    }

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            lastLat = location.latitude
                            lastLon = location.longitude
                            settingsViewModel.updateGeofence(context, lastLat, lastLon)
                            Toast.makeText(
                                context,
                                "Geofence set at: ${location.latitude}, ${location.longitude}",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Unable to get current location",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                ) {
                    Text("imposta nuova posizione")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "Vuoi eliminare il tuo Account?"
                )
                Text(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    text = "Quando eliminerai il tuo Account perderai qualsiasi informazione. Creandone uno nuovo dovrai collegare nuovamente i dispositivi."
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_red)),
                    onClick = {}
                ) {
                    Text("elimina account")
                }
            }
        }
    )
}

private const val REQUEST_LOCATION_PERMISSION = 1
