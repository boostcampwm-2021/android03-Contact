package com.ivyclub.contact.service.plan_reminder

import com.ivyclub.data.model.SimplePlanData

interface PlanReminderMaker {
    suspend fun makePlanReminders(planData: SimplePlanData)

    suspend fun cancelPlanReminder(planData: SimplePlanData)

    companion object {
        const val ACTION_ALARM = "com.ivyclub.contact.Alarm"
        const val REMINDER_TITLE = "reminder_title"
        const val REMINDER_TEXT = "reminder_text"
        const val REMINDER_PLAN_ID = "reminder_plan_id"
    }
}