package com.ivyclub.contact.util

import java.sql.Date
import java.util.*

const val HOUR_IN_MILLIS = (1000 * 60 * 60).toLong()
const val DAY_IN_MILLIS = HOUR_IN_MILLIS * 24L

private val calendar: Calendar by lazy { Calendar.getInstance() }

fun Date.getExactYear(): Int = getCalendar(this).get(Calendar.YEAR)

fun Date.getExactMonth(): Int = getCalendar(this).get(Calendar.MONTH) + 1

fun Date.getDayOfMonth(): Int = getCalendar(this).get(Calendar.DAY_OF_MONTH)

fun Date.getDayOfWeek(): DayOfWeek {
    return DayOfWeek.values()[getCalendar(this).get(Calendar.DAY_OF_WEEK) - 1]
}

fun Date.getHour(): Int = getCalendar(this).get(Calendar.HOUR_OF_DAY)

fun Date.getMinute(): Int = getCalendar(this).get(Calendar.MINUTE)

fun Date.getNewDate(year: Int, month: Int, dayOfMonth: Int): Date {
    getCalendar(this).set(year, month, dayOfMonth)
    return Date(calendar.time.time)
}

fun Date.getNewTime(hour: Int, minute: Int): Date {
    with(getCalendar(this)) {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        return Date(calendar.time.time)
    }
}

private fun getCalendar(date: Date): Calendar {
    calendar.time = date
    return calendar
}