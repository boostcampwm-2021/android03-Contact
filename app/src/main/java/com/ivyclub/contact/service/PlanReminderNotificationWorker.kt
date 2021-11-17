package com.ivyclub.contact.service

import android.content.Context
import androidx.core.text.isDigitsOnly
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.ivyclub.contact.R
import com.ivyclub.contact.util.*
import com.ivyclub.data.MyPreference
import com.ivyclub.data.MyPreference.Companion.NOTIFICATION_END
import com.ivyclub.data.MyPreference.Companion.NOTIFICATION_START
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.sql.Date
import java.util.*
import java.util.concurrent.TimeUnit

@HiltWorker
class PlanReminderNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notiPreferences: MyPreference
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        if (tags.isEmpty()) return Result.failure()

        val notiStart = notiPreferences.getNotificationTime(NOTIFICATION_START)
        val notiEnd = notiPreferences.getNotificationTime(NOTIFICATION_END)
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        if (currentHour in (notiStart until notiEnd) && notiPreferences.getNotificationState()) {
            inputData.getString(NOTI_TYPE)?.let { strNotiType ->
                return when (val notiType = NotificationType.valueOf(strNotiType)) {
                    NotificationType.MORNING -> doPlanMorningWork()
                    else -> doPlanReminderWork(notiType)
                }
            }
        }

        return Result.failure()
    }

    private fun doPlanMorningWork(): Result {
        PlanReminderNotification.makePlanNotification(
            context,
            context.getString(R.string.plan_morning_notification_title),
            context.getString(R.string.plan_morning_notification_content)
        )
        return Result.success()
    }

    private fun doPlanReminderWork(notiType: NotificationType): Result {
        val participants =
            inputData.getStringArray(DATA_PARTICIPANTS) ?: return Result.failure()

        val notiTitle: String
        val strFormat: String
        if (notiType == NotificationType.PLAN) {
            notiTitle = context.getString(R.string.plan_reminder_notification_title)
            strFormat = context.getString(R.string.format_plan_reminder_notification_solo)
        } else {
            notiTitle = context.getString(R.string.plan_night_notification_title)
            strFormat = context.getString(R.string.format_after_plan)
        }

        val notiText =
            if (participants.isEmpty()) {
                val planTitle = inputData.getString(DATA_PLAN_TITLE) ?: return Result.failure()
                String.format(strFormat, planTitle)
            } else {
                val firstParticipant = String.format(
                    context.getString(R.string.format_friend_title),
                    participants[0]
                )
                val textParticipants =
                    if (participants.size > 1) {
                        String.format(
                            context.getString(R.string.format_friend_count_etc),
                            firstParticipant,
                            participants.size - 1
                        )
                    } else firstParticipant
                String.format(strFormat, textParticipants)
            }

        val planId = tags.find { it.isDigitsOnly() }?.toLong()
        PlanReminderNotification.makePlanNotification(
            context,
            notiTitle,
            notiText,
            planId ?: -1L
        )

        return Result.success()
    }

    companion object {

        private const val DATA_PARTICIPANTS = "participants"
        private const val DATA_PLAN_TITLE = "title"
        private const val NOTI_TYPE = "notitype"

        fun setPlanAlarm(
            planId: Long,
            planTitle: String,
            participants: List<String>,
            planTime: Date,
            morningHour: Int,
            nightHour: Int,
            workManager: WorkManager
        ) {

            val delay = getDelayFromNow(planTime.time, HOUR_IN_MILLIS)
            if (delay < 0) return

            cancelPlanAlarms(planId, workManager)

            addAlarmTask(
                planId.toString(), workManager, delay, workDataOf(
                    DATA_PARTICIPANTS to participants.toTypedArray(),
                    DATA_PLAN_TITLE to planTitle,
                    NOTI_TYPE to NotificationType.PLAN.value
                )
            )

            val morningDelay = getDelayFromNow(planTime.getNewTime(morningHour, 10).time)
            if (morningDelay > 0) {
                addAlarmTask(
                    planId.toString(), workManager, morningDelay, workDataOf(
                        NOTI_TYPE to NotificationType.MORNING.value
                    )
                )
            }

            val nightDelay =
                getDelayFromNow(planTime.getNewTime(nightHour, 0).time, MINUTE_IN_MILLIS * 10)
            addAlarmTask(
                planId.toString(), workManager, nightDelay, workDataOf(
                    DATA_PARTICIPANTS to participants.toTypedArray(),
                    NOTI_TYPE to NotificationType.NIGHT.value
                )
            )
        }

        fun cancelPlanAlarms(planId: Long, workManager: WorkManager) {
            val planTag = planId.toString()
            workManager.cancelAllWorkByTag(planTag)
        }

        private fun addAlarmTask(
            tag: String,
            workManager: WorkManager,
            delay: Long,
            inputData: Data
        ) {
            val workRequest = OneTimeWorkRequestBuilder<PlanReminderNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .setInputData(inputData)
                .build()

            workManager.enqueue(workRequest)
        }

        private fun getDelayFromNow(targetTime: Long, early: Long = 0): Long =
            targetTime - System.currentTimeMillis() - early
    }
}