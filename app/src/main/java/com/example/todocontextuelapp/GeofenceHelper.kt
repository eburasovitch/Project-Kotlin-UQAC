package com.example.todocontextuelapp

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.todocontextuelapp.data.Routine
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

open class GeofenceHelper(private val context: Context) {

    protected open fun createGeofencingClient(): GeofencingClient {
        return LocationServices.getGeofencingClient(context)
    }

    fun addGeofence(routine: Routine) {
        if (routine.latitude == null || routine.longitude == null) return

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val geofence = Geofence.Builder()
                .setRequestId(routine.id.toString())
                .setCircularRegion(routine.latitude, routine.longitude, 100f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()

            val geofencingRequest = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build()

            val intent = android.content.Intent(context, GeofenceBroadcastReceiver::class.java)
            val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag)

            val client = createGeofencingClient()
            client.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener {
                    // Succès
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }
    }
}
