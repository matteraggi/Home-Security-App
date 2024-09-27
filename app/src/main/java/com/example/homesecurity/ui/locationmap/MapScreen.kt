package com.example.homesecurity.ui.locationmap

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homesecurity.R
import com.example.homesecurity.ui.settings.REQUEST_LOCATION_PERMISSION
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val settingsViewModel: MapViewModel = viewModel()

    settingsViewModel.initialize(context)

    var lastLat by remember { mutableDoubleStateOf(0.0) }
    var lastLon by remember { mutableDoubleStateOf(0.0) }

    var geofenceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val geofenceRadius = 75.0

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permissions granted, handle geofencing setup
        }
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lastLat = location.latitude
                    lastLon = location.longitude
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(lastLat, lastLon), 15f)
                }
            }

            val sharedPreferences = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE)
            val savedGeofenceLat = sharedPreferences.getFloat("geofenceLat", 0f).toDouble()
            val savedGeofenceLon = sharedPreferences.getFloat("geofenceLon", 0f).toDouble()
            geofenceLatLng = LatLng(savedGeofenceLat, savedGeofenceLon)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            if (lastLat != 0.0 && lastLon != 0.0) {
                val currentMarkerState = rememberMarkerState(position = LatLng(lastLat, lastLon))
                Marker(
                    state = currentMarkerState,
                    title = "Current Location",
                    icon = bitmapDescriptorFromVector(context, R.drawable.ic_baseline_person_24)
                )
            }

            if (geofenceLatLng.latitude != 0.0 && geofenceLatLng.longitude != 0.0) {
                val geofenceMarkerState = rememberMarkerState(position = geofenceLatLng)
                Marker(
                    state = geofenceMarkerState,
                    title = "Geofence Location",
                )
                Circle(
                    center = geofenceLatLng,
                    radius = geofenceRadius,
                    strokeColor = Color.Blue,
                    strokeWidth = 2f,
                    fillColor = Color.Blue.copy(alpha = 0.5f)
                )
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue_medium)),
            onClick = {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
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

                        geofenceLatLng = LatLng(lastLat, lastLon)

                        settingsViewModel.updateGeofence(context, lastLat, lastLon)

                        Toast.makeText(
                            context,
                            "Geofence set at: ${location.latitude}, ${location.longitude}",
                            Toast.LENGTH_LONG
                        ).show()

                        cameraPositionState.position = CameraPosition.fromLatLngZoom(geofenceLatLng, 15f)
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
            Text("Imposta nuova posizione")
        }
    }
}

fun bitmapDescriptorFromVector(context: Context, vectorResId: Int, scaleFactor: Float = 1.5f): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.let { vectorDrawable ->
        // Imposta le dimensioni originali
        val width = (vectorDrawable.intrinsicWidth * scaleFactor).toInt()
        val height = (vectorDrawable.intrinsicHeight * scaleFactor).toInt()

        vectorDrawable.setBounds(0, 0, width, height)

        // Crea un bitmap con le dimensioni ridimensionate
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)

        // Restituisce il bitmap come BitmapDescriptor
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
