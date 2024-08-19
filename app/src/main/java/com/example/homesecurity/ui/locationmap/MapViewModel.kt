package com.example.homesecurity.ui.locationmap

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.homesecurity.GeofenceReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class MapViewModel : ViewModel() {
    private lateinit var geofencingClient: GeofencingClient
    private val geofenceList = mutableListOf<Geofence>()

    private var currentGeofenceId: String? = null

    fun updateGeofence(context: Context, lat: Double, lon: Double) {
        geofencingClient = LocationServices.getGeofencingClient(context)
        val pendingIntent: PendingIntent by lazy {
            val intent = Intent(context, GeofenceReceiver::class.java)
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
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

        geofenceList.add(Geofence.Builder()
            .setRequestId("GEOFENCE_ID")
            .setCircularRegion(lat, lon, 1000f)
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
            addOnFailureListener {
                Log.d("GeofencingViewModel", "Failed to add geofence", it)
            }
        }
    }
}
