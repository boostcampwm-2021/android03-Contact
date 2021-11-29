package com.ivyclub.contact.service.plan_reminder

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date
import java.util.*
import java.util.concurrent.TimeUnit

@HiltWorker
class PlanReminderNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notiPreferences: MyPreference
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        if (tags.isEmpty()) return@withContext Result.failure()

        val notiStart = notiPreferences.getNotificationTime(NOTIFICATION_START)
        val notiEnd = notiPreferences.getNotificationTime(NOTIFICATION_END)
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        if (currentHour in (notiStart until notiEnd) && notiPreferences.getNotificationState()) {
            inputData.getString(NOTI_TYPE)?.let { strNotiType ->
                return@withContext when (val notiType = NotificationType.valueOf(strNotiType)) {
                    NotificationType.MORNING -> doPlanMorningWork()
                    else -> doPlanReminderWork(notiType)
                }
            }
        }

        return@withContext Result.failure()
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
            strFormat =
                context.getString(
                    if (participants.isEmpty()) R.string.format_plan_reminder_notification_solo
                    else R.string.format_plan_reminder_notification_with_friends
                )
        } else {
            notiTitle = context.getString(R.string.plan_night_notification_title)
            strFormat =
                if (participants.isEmpty()) context.getString(R.string.format_after_plan_with_friends)
                else context.getString(R.string.format_after_plan)
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
        private const val TAG_NOTI_START = "start_"
        private const val TAG_NOTI_END = "end_"

        fun setPlanAlarm(
            planId: Long,
            planTitle: String,
            participants: List<String>,
            planTime: Date,
            startHour: Int,
            endHour: Int,
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

            setDayStartAlarm(planId, startHour, planTime, delay, workManager)
            setDayEndAlarm(planId, endHour, planTime, delay, participants, workManager)
        }

        private fun setDayStartAlarm(
            planId: Long,
            startHour: Int,
            planTime: Date,
            delay: Long,
            workManager: WorkManager
        ) {
            val morningDelay = getDelayFromNow(planTime.getNewTime(startHour, 10).time)
            if (morningDelay in 1 until delay) {
                addAlarmTask(
                    planId.toString() + TAG_NOTI_START, workManager, morningDelay, workDataOf(
                        NOTI_TYPE to NotificationType.MORNING.value
                    )
                )
            }
        }

        private fun setDayEndAlarm(
            planId: Long,
            endHour: Int,
            planTime: Date,
            delay: Long,
            participants: List<String>,
            workManager: WorkManager
        ) {
            val nightDelay =
                getDelayFromNow(planTime.getNewTime(endHour, 0).time, MINUTE_IN_MILLIS * 10)
            if (nightDelay > delay) {
                addAlarmTask(
                    planId.toString() + TAG_NOTI_END, workManager, nightDelay, workDataOf(
                        DATA_PARTICIPANTS to participants.toTypedArray(),
                        NOTI_TYPE to NotificationType.NIGHT.value
                    )
                )
            }
        }

        fun cancelPlanAlarms(planId: Long, workManager: WorkManager) {
            val planTag = planId.toString()
            workManager.cancelAllWorkByTag(planTag)

            cancelDayStartEndAlarms(planId, workManager)
        }

        private fun cancelDayStartEndAlarms(planId: Long, workManager: WorkManager) {
            val planTag = planId.toString()

            val notiStartTag = planTag + TAG_NOTI_START
            workManager.cancelAllWorkByTag(notiStartTag)

            val notiEndTag = planTag + TAG_NOTI_END
            workManager.cancelAllWorkByTag(notiEndTag)
        }

        fun resetDayStartEndAlarms(
            planId: Long,
            startHour: Int,
            endHour: Int,
            planTime: Date,
            participants: List<String>,
            workManager: WorkManager
        ) {
            cancelDayStartEndAlarms(planId, workManager)

            val delay = getDelayFromNow(planTime.time, HOUR_IN_MILLIS)
            setDayStartAlarm(planId, startHour, planTime, delay, workManager)
            setDayEndAlarm(planId, endHour, planTime, delay, participants, workManager)
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