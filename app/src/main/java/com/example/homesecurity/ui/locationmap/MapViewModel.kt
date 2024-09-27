package com.example.homesecurity.ui.locationmap

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.homesecurity.GeofenceReceiver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class MapViewModel : ViewModel() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    private val geofenceList = mutableListOf<Geofence>()
    private var currentGeofenceId: String? = null
    private var isGeofenceActive = false


    fun initialize(context: Context) {
        if (!::fusedLocationClient.isInitialized) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }
        if (!::geofencingClient.isInitialized) {
            geofencingClient = LocationServices.getGeofencingClient(context)
        }
    }

    fun isGeofenceEnabled(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("geofenceEnabled", true)
    }

    fun updateGeofence(context: Context, lat: Double, lon: Double) {
        if (!::geofencingClient.isInitialized) {
            Log.e("MapViewModel", "GeofencingClient is not initialized")
            return
        }
        // Assicurati di chiamare initialize() prima di utilizzare questa funzione
        val pendingIntent: PendingIntent by lazy {
            val intent = Intent(context, GeofenceReceiver::class.java)
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        }
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
            .setCircularRegion(lat, lon, 75f)
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

                isGeofenceActive = true

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

    fun restoreGeofence(context: Context) {
        val sharedPreferences = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getFloat("geofenceLat", 0f).toDouble()
        val lon = sharedPreferences.getFloat("geofenceLon", 0f).toDouble()

        if (lat != 0.0 && lon != 0.0) {
            updateGeofence(context, lat, lon) // Riattiva il geofence con i valori salvati
        } else {
            Log.e("GeofencingViewModel", "No geofence coordinates saved")
        }
    }



    fun disableGeofence(context: Context) {
        currentGeofenceId?.let { geofenceId ->
            geofencingClient.removeGeofences(listOf(geofenceId)).run {
                addOnSuccessListener {
                    Log.d("GeofencingViewModel", "Geofence disabled successfully")
                    isGeofenceActive = false
                    saveGeofenceState(context, false)
                }
                addOnFailureListener {
                    Log.d("GeofencingViewModel", "Failed to disable geofence")
                }
            }
        }
    }

    // Salva lo stato del geofence nelle SharedPreferences
    fun saveGeofenceState(context: Context, isActive: Boolean) {
        val sharedPreferences = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("geofenceEnabled", isActive).apply()
    }

    override fun onCleared() {
        super.onCleared()
        // Pulizia se necessario
    }
}
