package com.example.todocontextuelapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todocontextuelapp.data.Routine
import java.text.SimpleDateFormat
import java.util.*

open class TimeNotificationHelper(private val context: Context) {

    protected open fun getAlarmManager(): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    protected open fun createNotificationPendingIntent(routine: Routine): PendingIntent {
        val intent = Intent(context, TimeNotificationReceiver::class.java).apply {
            putExtra("title", "Routine Reminder")
            putExtra("message", "It's time for: ${routine.name}")
        }
        // On utilise l'id de la routine en code de requête pour éviter les collisions
        return PendingIntent.getBroadcast(
            context,
            routine.id ?: System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    fun scheduleRoutineNotification(routine: Routine) {
        Log.d("TimeNotificationHelper", "scheduleRoutineNotification called for routine: ${routine.name}")

        val calendar = parseDateTime(routine.date, routine.hour, routine.minute, routine.amPm) ?: return

        val alarmManager = getAlarmManager()
        val pendingIntent = createNotificationPendingIntent(routine)

        when (routine.frequency) {
            "Just for this time" -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }
            "Every day" -> {
                // Intervalle de 24h
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            "Once a week" -> {
                // Intervalle de 7 jours
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
                )
            }
            else -> {
                // Aucune notification
            }
        }
    }

    private fun parseDateTime(dateStr: String, hour: Int, minute: Int, amPm: String): Calendar? {
        return try {
            // Création du SimpleDateFormat avec le fuseau horaire souhaité
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("America/Toronto")
            val date = sdf.parse(dateStr) ?: return null
            // Création d'un Calendar en spécifiant explicitement le fuseau "America/Toronto"
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Toronto")).apply {
                time = date

                // Conversion de l'heure en format 24h
                var finalHour = hour
                if (amPm.equals("PM", ignoreCase = true) && hour < 12) {
                    finalHour += 12
                } else if (amPm.equals("AM", ignoreCase = true) && hour == 12) {
                    finalHour = 0
                }

                set(Calendar.HOUR_OF_DAY, finalHour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
            Log.d("TimeNotificationHelper", "Scheduled time: ${calendar.time} vs now: ${Date()}")
            calendar
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
