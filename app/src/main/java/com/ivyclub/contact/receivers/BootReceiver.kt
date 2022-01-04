package com.ivyclub.contact.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker
import com.ivyclub.contact.util.getNewTime
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: ContactRepository

    @Inject
    lateinit var reminderMaker: PlanReminderMaker

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val todayStart = Date(System.currentTimeMillis()).getNewTime(0, 0).time
                repository.getPlanListAfter(todayStart).forEach { planData ->
                    reminderMaker.makePlanReminders(planData)
                }
            }
        }
    }
}