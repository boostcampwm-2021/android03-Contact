package com.ivyclub.contact.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.ACTION_ALARM
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_PLAN_ID
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_TEXT
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_TITLE
import com.ivyclub.contact.service.plan_reminder.PlanReminderNotification
import com.ivyclub.contact.util.getHour
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: ContactRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        if (intent.action == ACTION_ALARM) {

            val alarmState = repository.getNotificationState()
            val startHour = repository.getStartAlarmHour()
            val endHour = repository.getEndAlarmHour()
            val currentHour = Date(System.currentTimeMillis()).getHour()

            if (!alarmState || currentHour < startHour || currentHour >= endHour) return

            val notiTitle = intent.getStringExtra(REMINDER_TITLE)
            val notiText = intent.getStringExtra(REMINDER_TEXT)
            val planId = intent.getLongExtra(REMINDER_PLAN_ID, -1L)

            if (notiTitle != null && notiText != null && context != null) {
                PlanReminderNotification.makePlanNotification(
                    context,
                    notiTitle,
                    notiText,
                    planId
                )
            }
        }
    }
}