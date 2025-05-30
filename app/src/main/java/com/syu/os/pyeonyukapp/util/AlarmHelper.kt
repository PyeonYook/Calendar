package com.syu.os.pyeonyukapp.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.syu.os.pyeonyukapp.model.Event
import com.syu.os.pyeonyukapp.receiver.AlarmReceiver
import java.time.ZoneId

object AlarmHelper {

    fun scheduleAlarm(context: Context, event: Event) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", event.title)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // reminder 시간 파싱하여 알림 시각 계산
        val reminderMinutes = parseReminder(event.reminder)
        val alarmTime = event.startDateTime
            .minusMinutes(reminderMinutes.toLong())
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, event: Event) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun parseReminder(reminder: String): Int {
        return when {
            reminder.contains("분") -> {
                Regex("""(\d+)""").find(reminder)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            }
            reminder.contains("시간") -> {
                val hours = Regex("""(\d+)""").find(reminder)?.groupValues?.get(1)?.toIntOrNull() ?: 0
                hours * 60
            }
            else -> 0
        }
    }
}
