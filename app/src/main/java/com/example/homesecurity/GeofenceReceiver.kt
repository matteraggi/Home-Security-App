package com.example.homesecurity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.homesecurity.ui.home.changeButtonStateTo
import com.example.homesecurity.ui.home.getCurrentUserId
import com.example.homesecurity.ui.home.getHomePeople
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeofenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        Log.d("GEOFENCING", "Received something")

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            val errorMessage = geofencingEvent?.let {
                GeofenceStatusCodes.getStatusCodeString(it.errorCode)
            } ?: "No geofencing event found!"
            Log.e("GEOFENCE", errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Get the transition details as a String.
            val geofenceDetails = triggeringGeofences.toString()
            Toast.makeText(context, geofenceDetails, Toast.LENGTH_LONG).show()

            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                handleGeofenceExit()
            } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                handleGeofenceEnter()
            }
        }
    }

    private fun handleGeofenceExit() {
        CoroutineScope(Dispatchers.IO).launch {
            val people = getHomePeople(getCurrentUserId())
            val anyInside = people.any { it.inside }

            if (!anyInside) {
                // If no persons inside, activate the alarm
                changeButtonStateTo(true)
            }
        }
    }

    private fun handleGeofenceEnter() {
        // Deactivate the alarm when entering the geofence
        CoroutineScope(Dispatchers.IO).launch {
            changeButtonStateTo(false)
        }
    }

}
