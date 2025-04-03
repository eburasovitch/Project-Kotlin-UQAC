package com.example.todocontextuelapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
        return PendingIntent.getBroadcast(
            context,
            routine.id ?: System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    fun scheduleRoutineNotification(routine: Routine) {
        val calendar = parseDateTime(routine.date, routine.hour, routine.amPm) ?: return

        val alarmManager = getAlarmManager()
        val pendingIntent = createNotificationPendingIntent(routine)

        when (routine.frequency) {
            "Just for this time" -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    // Sur API 23+, on peut appeler setExactAndAllowWhileIdle
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    // Sur API < 23, fallback sur setExact
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }
            "Every day" -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            "Once a week" -> {
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

    private fun parseDateTime(dateStr: String, hour: Int, amPm: String): Calendar? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val date = sdf.parse(dateStr) ?: return null
            val calendar = Calendar.getInstance().apply {
                time = date
                var finalHour = hour
                if (amPm.equals("PM", ignoreCase = true) && hour < 12) {
                    finalHour += 12
                } else if (amPm.equals("AM", ignoreCase = true) && hour == 12) {
                    finalHour = 0
                }
                set(Calendar.HOUR_OF_DAY, finalHour)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            calendar
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
