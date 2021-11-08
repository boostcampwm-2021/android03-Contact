package com.ivyclub.contact.util

import java.sql.Date
import java.util.*

const val DAY_IN_MILLIS = (1000 * 60 * 60 * 24).toLong()

private val calendar: Calendar by lazy { Calendar.getInstance() }

fun Date.getExactYear(): Int = getCalendar(this).get(Calendar.YEAR)

fun Date.getExactMonth(): Int = getCalendar(this).get(Calendar.MONTH) + 1

fun Date.getDayOfMonth(): Int = getCalendar(this).get(Calendar.DAY_OF_MONTH)

fun Date.getDayOfWeek(): DayOfWeek {
    return DayOfWeek.values()[getCalendar(this).get(Calendar.DAY_OF_WEEK) - 1]
}

private fun getCalendar(date: Date): Calendar {
    calendar.time = date
    return calendar
}