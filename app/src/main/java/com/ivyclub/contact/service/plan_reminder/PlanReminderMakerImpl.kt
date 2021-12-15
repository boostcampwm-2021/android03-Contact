package com.ivyclub.contact.service.plan_reminder

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import com.ivyclub.contact.R
import com.ivyclub.contact.receivers.AlarmReceiver
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.ACTION_ALARM
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_PLAN_ID
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_TEXT
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker.Companion.REMINDER_TITLE
import com.ivyclub.contact.util.*
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.SimplePlanData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.sql.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanReminderMakerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ContactRepository
) : PlanReminderMaker {

    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    override suspend fun makePlanReminders(planData: SimplePlanData) {

        val participants = planData.participant.map { participantId ->
            repository.getSimpleFriendDataById(participantId).name
        }

        makeStartReminder(planData)
        makeLastPlanReminder(planData, participants)
        makeEndReminder(planData, participants)
    }

    override suspend fun cancelPlanReminder(planData: SimplePlanData) {
        cancelStartReminder(planData.date)
        cancelLastReminder(planData.id)
        cancelEndReminder(planData.id)
    }

    private fun makeLastPlanReminder(
        planData: SimplePlanData,
        participants: List<String>
    ) {
        if (System.currentTimeMillis() > planData.date.time) return

        val reminderTitle = context.getString(R.string.plan_reminder_notification_title)

        val reminderText: String
        val strFormat: String
        if (participants.isEmpty()) {
            strFormat = context.getString(R.string.format_plan_reminder_notification_solo)
            reminderText = String.format(strFormat, planData.title)
        }
        else {
            strFormat = context.getString(R.string.format_plan_reminder_notification_with_friends)
            val firstParticipant = String.format(
                context.getString(R.string.format_friend_title),
                participants.first()
            )
            val textParticipants =
                if (participants.size > 1) {
                    String.format(
                        context.getString(R.string.format_friend_count_etc),
                        firstParticipant,
                        participants.size - 1
                    )
                } else firstParticipant
            reminderText = String.format(strFormat, textParticipants)
        }

        val intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(REMINDER_TITLE, reminderTitle)
                putExtra(REMINDER_TEXT, reminderText)
                putExtra(REMINDER_PLAN_ID, planData.id)
                action = ACTION_ALARM
            }

        getReminderPendingIntent(planData.id.toInt(), intent)?.let { pendingIntent ->
            setAlarm(planData.date.time - HOUR_IN_MILLIS, pendingIntent)
        }
    }

    private fun makeStartReminder(planData: SimplePlanData) {

        val startTime =
            planData.date.getNewTime(repository.getStartAlarmHour(), 0).time

        if (System.currentTimeMillis() > startTime) return

        val reminderTitle = context.getString(R.string.plan_morning_notification_title)
        val reminderText = context.getString(R.string.plan_morning_notification_content)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(REMINDER_TITLE, reminderTitle)
            putExtra(REMINDER_TEXT, reminderText)
            action = ACTION_ALARM
        }

        getReminderPendingIntent(getReminderRequestCodeWithPlanDate(planData.date), intent)?.let { pendingIntent ->
            setAlarm(startTime, pendingIntent)
        }
    }

    private fun makeEndReminder(
        planData: SimplePlanData,
        participants: List<String>
    ) {
        val endTime =
            planData.date.getNewTime(repository.getEndAlarmHour(), 0).time - (10 * MINUTE_IN_MILLIS)

        if (System.currentTimeMillis() > endTime) return

        val reminderTitle = context.getString(R.string.plan_night_notification_title)

        val reminderText: String
        val strFormat: String
        if (participants.isEmpty()) {
            strFormat = context.getString(R.string.format_after_plan)
            reminderText = String.format(strFormat, planData.title)
        }
        else {
            strFormat = context.getString(R.string.format_after_plan_with_friends)
            val firstParticipant = String.format(
                context.getString(R.string.format_friend_title),
                participants.first()
            )
            val textParticipants =
                if (participants.size > 1) {
                    String.format(
                        context.getString(R.string.format_friend_count_etc),
                        firstParticipant,
                        participants.size - 1
                    )
                } else firstParticipant
            reminderText = String.format(strFormat, textParticipants)
        }

        val intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(REMINDER_TITLE, reminderTitle)
                putExtra(REMINDER_TEXT, reminderText)
                putExtra(REMINDER_PLAN_ID, planData.id)
                action = ACTION_ALARM
            }

        getReminderPendingIntent((-(planData.id)).toInt(), intent)?.let { pendingIntent ->
            setAlarm(endTime, pendingIntent)
        }
    }

    private fun cancelLastReminder(planId: Long) {
        cancelAlarm(planId.toInt())
    }

    private suspend fun cancelStartReminder(planDate: Date) {
        val planDayStartTime = planDate.getNewTime(repository.getStartAlarmHour(), 0).time
        val planDayEndTime = planDate.getNewTime(repository.getEndAlarmHour(), 0).time
        val plansOnSameDay = repository.getPlanListAfter(planDayStartTime).filter { it.date.time < planDayEndTime }
        if (plansOnSameDay.isEmpty()) {
            cancelAlarm(getReminderRequestCodeWithPlanDate(planDate))
        }
    }

    private fun cancelEndReminder(planId: Long) {
        cancelAlarm(-planId.toInt())
    }

    private fun getReminderRequestCodeWithPlanDate(planDate: Date): Int {
        val tmpStrRequestCode =
            "${planDate.getExactYear()}${planDate.getExactMonth()}${planDate.getDayOfMonth()}"
        return tmpStrRequestCode.toInt()
    }

    private fun getReminderPendingIntent(
        requestCode: Int,
        intent: Intent,
        flag: Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
    ): PendingIntent? {
        return PendingIntent
            .getBroadcast(
                context,
                requestCode,
                intent,
                flag
            )
    }

    private fun setAlarm(time: Long, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                RTC_WAKEUP, time, pendingIntent
            )
        } else {
            alarmManager.setExact(
                RTC_WAKEUP, time, pendingIntent
            )
        }
    }

    private fun cancelAlarm(requestCode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_ALARM
        }
        getReminderPendingIntent(requestCode, intent)?.let { pendingIntent ->
            alarmManager.cancel(pendingIntent)
        }
    }
}