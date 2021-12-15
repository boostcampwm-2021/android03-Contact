package com.ivyclub.contact.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.ACTION_ALARM
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_PLAN_ID
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_TEXT
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_TITLE
import com.ivyclub.contact.service.plan_reminder.PlanReminderNotification

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        if (intent.action == ACTION_ALARM) {
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