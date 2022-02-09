package com.ivyclub.contact.ui.plan_list

import com.ivyclub.contact.util.*
import com.ivyclub.data.model.SimplePlanData
import java.sql.Date

data class PlanListItemViewModel(
    private val planData: SimplePlanData,
    val friends: List<String>
) {
    val id = planData.id
    private val date: Date = planData.date
    val dayCount = date.time / DAY_IN_MILLIS
    val planMonth = date.getExactMonth()
    val planYear = date.getExactYear()
    val planDayOfMonth = date.getDayOfMonth()
    val planDayOfWeek = date.getDayOfWeek()
    val title: String = planData.title
}