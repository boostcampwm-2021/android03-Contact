package com.ivyclub.contact.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
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
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notiPreferences: MyPreference
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val notiStart = notiPreferences.getNotificationTime(NOTIFICATION_START)
        val notiEnd = notiPreferences.getNotificationTime(NOTIFICATION_END)
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        if (currentHour in (notiStart..notiEnd) && notiPreferences.getNotificationState()) {
            // TODO: Notification 올리기
            PlanReminderNotification.makeNotification(context)
        }

        return Result.success()
    }

    companion object {
        fun setPlanAlarm(planId: Long, planTime: Date, workManager: WorkManager) {

            val delay = getDelayFromNow(planTime)
            if (delay < 0) return

            val planTag = planId.toString()
            workManager.cancelAllWorkByTag(planTag)

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(planTag)
                .build()

            workManager.enqueue(workRequest)
        }

        private fun getDelayFromNow(planDate: Date): Long =
            planDate.time - System.currentTimeMillis() - HOUR_IN_MILLIS
    }
}