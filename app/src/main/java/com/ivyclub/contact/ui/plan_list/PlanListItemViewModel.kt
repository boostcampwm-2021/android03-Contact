package com.ivyclub.contact.ui.plan_list

import com.ivyclub.contact.util.*
import com.ivyclub.data.model.AppointmentData
import java.sql.Date

data class PlanListItemViewModel(private val appointmentData: AppointmentData) {
    val id = appointmentData.id

    private val date: Date = appointmentData.date
    val dayCount = date.time / DAY_IN_MILLIS
    val planMonth = date.getExactMonth()
    val planYear = date.getExactYear()
    val planDayOfMonth = date.getDayOfMonth()
    val planDayOfWeek = date.getDayOfWeek().korean

    val title: String = appointmentData.title
    val friendCount = appointmentData.participant.size
    val friends: List<String> = appointmentData.participant
        .subList(0, 3.coerceAtMost(friendCount))
}