package com.example.homesecurity.ui.settings

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.homesecurity.GeofenceReceiver
import com.google.android.gms.location.*

class SettingsViewModel : ViewModel() {
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var geofencingClient: GeofencingClient

    private var currentGeofenceId: String? = null

    init {
        locationRequest = LocationRequest.Builder(1000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    // Update UI with location data
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    @SuppressLint("MissingPermission")
    fun updateGeofence(context: Context, lat: Double, lon: Double) {
        geofencingClient = LocationServices.getGeofencingClient(context)
        val pendingIntent: PendingIntent by lazy {
            val intent = Intent(context, GeofenceReceiver::class.java)
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // Remove the current geofence if it exists
        currentGeofenceId?.let { geofenceId ->
            geofencingClient.removeGeofences(listOf(geofenceId)).run {
                addOnSuccessListener {
                    Log.d("GeofencingViewModel", "Geofence removed successfully")
                }
                addOnFailureListener {
                    Log.d("GeofencingViewModel", "Failed to remove geofence")
                }
            }
        }

        // Add the new geofence
        val geofence = Geofence.Builder()
            .setRequestId("NEW_GEOFENCE_ID")
            .setCircularRegion(lat, lon, 1000f)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(geofencingRequest, pendingIntent).run {
            addOnSuccessListener {
                Log.d("GeofencingViewModel", "Geofence added successfully")
                currentGeofenceId = geofence.requestId
            }
            addOnFailureListener {
                Log.d("GeofencingViewModel", "Failed to add geofence")
            }
        }
    }
}
