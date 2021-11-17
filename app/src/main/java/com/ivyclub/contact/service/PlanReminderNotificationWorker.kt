package com.ivyclub.contact.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.ivyclub.contact.R
import com.ivyclub.contact.util.HOUR_IN_MILLIS
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

        if (currentHour in (notiStart..notiEnd) && notiPreferences.getNotificationState()) {

            val participants =
                inputData.getStringArray(DATA_PARTICIPANTS) ?: return Result.failure()

            val notiText =
                if (participants.isEmpty()) {
                    val planTitle = inputData.getString(DATA_PLAN_TITLE) ?: return Result.failure()
                    String.format(
                        context.getString(R.string.format_plan_reminder_notification_solo),
                        planTitle
                    )
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
                    String.format(
                        context.getString(R.string.format_plan_reminder_notification_with_friends),
                        textParticipants
                    )
                }

            val planId = tags.toList()[0].toLong()
            PlanReminderNotification.makeNotification(context, planId, notiText)

            return Result.success()
        }

        return Result.failure()
    }

    companion object {

        private const val DATA_PARTICIPANTS = "participants"
        private const val DATA_PLAN_TITLE = "title"

        fun setPlanAlarm(
            planId: Long,
            planTitle: String,
            participants: List<String>,
            planTime: Date,
            workManager: WorkManager
        ) {

            val delay = getDelayFromNow(planTime)
            if (delay < 0) return

            cancelPlanAlarm(planId, workManager)

            val workRequest = OneTimeWorkRequestBuilder<PlanReminderNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(planId.toString())
                .setInputData(
                    workDataOf(
                        DATA_PARTICIPANTS to participants.toTypedArray(),
                        DATA_PLAN_TITLE to planTitle
                    )
                )
                .build()

            workManager.enqueue(workRequest)
        }

        fun cancelPlanAlarm(planId: Long, workManager: WorkManager) {
            val planTag = planId.toString()
            workManager.cancelAllWorkByTag(planTag)
        }

        private fun getDelayFromNow(planDate: Date): Long =
            planDate.time - System.currentTimeMillis() - HOUR_IN_MILLIS
    }
}