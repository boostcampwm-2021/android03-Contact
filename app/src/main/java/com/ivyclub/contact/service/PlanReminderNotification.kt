package com.ivyclub.contact.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ivyclub.contact.R
import com.ivyclub.contact.ui.main.MainActivity

object PlanReminderNotification {
    private const val CHANNEL_ID = "plan_reminder"
    private const val CHANNEL_NAME = "contact plans"
    const val NOTIFICATION = "notification"
    const val NOTI_PLAN_ID = "noti_plan_id"

    private var notificationManager: NotificationManager? = null

    fun makePlanNotification(context: Context, title: String, text: String, planId: Long = -1L) {

        checkNotificationManager(context)
        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(NOTIFICATION, true)
            putExtra(NOTI_PLAN_ID, planId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            planId.toInt(),
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setColor(ContextCompat.getColor(context, R.color.green_200))
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(planId.toInt(), notification)
    }

    private fun checkNotificationManager(context: Context) {
        if (notificationManager == null) {
            notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (notificationManager?.getNotificationChannel(CHANNEL_ID) != null) return

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