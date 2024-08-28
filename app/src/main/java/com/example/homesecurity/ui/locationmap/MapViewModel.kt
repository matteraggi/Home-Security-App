package com.example.homesecurity.ui.locationmap

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.homesecurity.GeofenceReceiver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MapViewModel : ViewModel() {
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val geofenceList = mutableListOf<Geofence>()
    private var currentGeofenceId: String? = null

    fun updateGeofence(context: Context, lat: Double, lon: Double) {
        geofencingClient = LocationServices.getGeofencingClient(context)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val pendingIntent: PendingIntent by lazy {
            val intent = Intent(context, GeofenceReceiver::class.java)
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        }


        // Setup location request
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).apply {
            setMinUpdateIntervalMillis(2000) // Intervallo minimo di aggiornamento
            setWaitForAccurateLocation(false) // Attendi o meno una posizione accurata
            setMaxUpdateDelayMillis(10000) // Massimo ritardo di aggiornamento
        }.build()

        // Setup location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    Log.d("MapViewModel", "Location: ${location.latitude}, ${location.longitude}")
                }
            }
        }

        // Start location updates
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

        // Rimozione e aggiunta del geofence come nel tuo codice originale
        currentGeofenceId?.let { geofenceId ->
            geofencingClient.removeGeofences(listOf(geofenceId)).run {
                addOnSuccessListener { Log.d("GeofencingViewModel", "Geofence removed successfully") }
                addOnFailureListener { Log.d("GeofencingViewModel", "Failed to remove geofence") }
            }
        }

        geofenceList.add(Geofence.Builder()
            .setRequestId("GEOFENCE_ID")
            .setCircularRegion(lat, lon, 20f)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build())

        val geofencingRequest = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()

        geofencingClient.addGeofences(geofencingRequest, pendingIntent).run {
            addOnSuccessListener {
                Log.d("GeofencingViewModel", "Geofence added successfully: ${lat.toFloat()}, ${lon.toFloat()}")
                currentGeofenceId = geofenceList[0].requestId

                val sharedPreferences = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("geofenceId", currentGeofenceId).apply()
                sharedPreferences.edit().putFloat("geofenceLat", lat.toFloat()).apply()
                sharedPreferences.edit().putFloat("geofenceLon", lon.toFloat()).apply()
            }
            addOnFailureListener { Log.d("GeofencingViewModel", "Failed to add geofence", it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Stop location updates when ViewModel is cleared
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}