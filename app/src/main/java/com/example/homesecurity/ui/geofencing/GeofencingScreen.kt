package com.example.homesecurity.ui.geofencing

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices

@Composable
fun GeofencingScreen() {
    val context = LocalContext.current
    val geofencingViewModel: GeofencingViewModel = viewModel()

    var lastLat by remember { mutableStateOf(0.0) }
    var lastLon by remember { mutableStateOf(0.0) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        geofencingViewModel.startLocationUpdates(fusedLocationClient)
    }

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Button(onClick = {
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
                            geofencingViewModel.updateGeofence(context, lastLat, lastLon)
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
                }) {
                    Text("Imposta il nuovo geofencing qui")
                }
            }
        }
    )
}

private const val REQUEST_LOCATION_PERMISSION = 1
