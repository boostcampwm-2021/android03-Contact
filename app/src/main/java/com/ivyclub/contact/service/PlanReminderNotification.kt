package com.ivyclub.contact.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ivyclub.contact.R

object PlanReminderNotification {
    private const val CHANNEL_ID = "plan_reminder"
    private const val CHANNEL_NAME = "contact plans"

    private var notificationManager: NotificationManager? = null

    fun makeNotification(context: Context, text: String) {

        if (notificationManager == null) {
            notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
        }

        createNotificationChannel()

        // TODO: PendingIntent 설정

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(context.getString(R.string.plan_reminder_notification_title))
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(0, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notiChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(true)
            }

            notificationManager?.createNotificationChannel(notiChannel)
        }
    }
}