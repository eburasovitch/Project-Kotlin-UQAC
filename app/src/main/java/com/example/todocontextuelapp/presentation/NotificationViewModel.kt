package com.example.todocontextuelapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManagerCompat,
    private val notificationBuilder: NotificationCompat.Builder
) : ViewModel() {

    fun showSimpleNotification() {
        if (hasNotificationPermission()) {
            try {
                notificationManager.notify(1, notificationBuilder.build())
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    fun updateNotification() {
        if (hasNotificationPermission()) {
            try {
                notificationManager.notify(1,
                    notificationBuilder.setContentTitle("Updated Title").build())
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    fun cancelNotification() {
        notificationManager.cancel(1)
    }

    private fun hasNotificationPermission(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}
