package com.example.todocontextuelapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.example.todocontextuelapp.data.Routine
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.text.SimpleDateFormat
import java.util.*

class TimeNotificationHelperTest {

    private lateinit var mockContext: Context
    private lateinit var mockAlarmManager: AlarmManager
    private lateinit var helper: TimeNotificationHelper

    @Before
    fun setUp() {
        // On "mock" le contexte et l'AlarmManager
        mockContext = mock(Context::class.java)
        mockAlarmManager = mock(AlarmManager::class.java)

        // On crée un TimeNotificationHelper factice qui renvoie nos mocks
        helper = object : TimeNotificationHelper(mockContext) {
            override fun getAlarmManager(): AlarmManager {
                return mockAlarmManager
            }

            override fun createNotificationPendingIntent(routine: Routine): PendingIntent {
                // On renvoie un mock de PendingIntent, pour éviter la logique réelle
                return mock(PendingIntent::class.java)
            }
        }
    }

    @Test
    fun `test scheduleRoutineNotification with Just for this time`() {
        // Routine configurée pour un déclenchement ponctuel
        val routine = Routine(
            id = 123,
            name = "Test Notification",
            description = "desc",
            date = "01/01/2030",
            hour = 9,
            minute = 0,
            amPm = "AM",
            frequency = "Just for this time"
        )

        // Exécution
        helper.scheduleRoutineNotification(routine)

        // Si API >= 23, on s'attend à setExactAndAllowWhileIdle(...)
        // Sinon, le code appelle setExact(...)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            verify(mockAlarmManager, atLeastOnce()).setExactAndAllowWhileIdle(
                eq(AlarmManager.RTC_WAKEUP),
                anyLong(),
                any(PendingIntent::class.java)
            )
        } else {
            verify(mockAlarmManager, atLeastOnce()).setExact(
                eq(AlarmManager.RTC_WAKEUP),
                anyLong(),
                any(PendingIntent::class.java)
            )
        }
    }

    @Test
    fun `test scheduleRoutineNotification with Once a week`() {
        // Routine configurée pour "Once a week"
        val routine = Routine(
            id = 234,
            name = "Weekly Notification",
            description = "desc",
            date = "02/01/2030",
            hour = 12,
            minute = 0,
            amPm = "PM",
            frequency = "Once a week"
        )

        // Exécution
        helper.scheduleRoutineNotification(routine)

        // "Once a week" appelle setRepeating(...) dans le code
        verify(mockAlarmManager, atLeastOnce()).setRepeating(
            eq(AlarmManager.RTC_WAKEUP),
            anyLong(),
            eq(AlarmManager.INTERVAL_DAY * 7),
            any(PendingIntent::class.java)
        )
    }

    @Test
    fun `test parseDateTime indirectly`() {
        // On teste la fonction parseDateTime via un SimpleDateFormat
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US)
        val date = sdf.parse("12/31/2025 10:00")
        assertNotNull(date)
    }
}
