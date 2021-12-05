package com.ivyclub.contact.util

import java.util.*

object StringManager {
    fun getString(targetString: String): String {
        when (Locale.getDefault().language) {
            "ko" -> { // 한국어일 때
                return targetString
            }
            else -> { // 영어이거나 지원되지 않는 언어일 때
                return when (targetString) {
                    "친구" -> "Friend"
                    "즐겨찾기" -> "Favorite"
                    "일" -> "Sun"
                    "월" -> "Mon"
                    "화" -> "Tue"
                    "수" -> "Wed"
                    "목" -> "Thu"
                    "금" -> "Fri"
                    "토" -> "Sat"
                    else -> "-"
                }
            }
        }
    }

    fun getDateFormatBy(planDayOfMonth: String, planDayOfWeek: String): String {
        return if (Locale.getDefault().language == "ko") {
            "${planDayOfMonth}일 ${planDayOfWeek}요일"
        } else {
            "$planDayOfMonth $planDayOfWeek"
        }
    }

    fun getMonthFormatBy(month: Int): String {
        return if (Locale.getDefault().language == "ko") { // 한글일 때
            "${month}월"
        } else { // 영어가 그 외 언어일 때
            Month.EngMonth.values()[month - 1].name
        }
    }
}