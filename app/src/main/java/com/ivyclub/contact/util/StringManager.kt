package com.ivyclub.contact.util

import android.util.Log
import java.util.*

object StringManager {
    fun getString(targetString: String): String {
        Log.d(this::class.simpleName, "getString , $targetString")
        when (Locale.getDefault().language) {
            "ko" -> { // 한국어일 때
                return targetString
            }
            "zh" -> {
                return when (targetString) {
                    "친구" -> "好友"
                    "즐겨찾기" -> "收藏"
                    "일" -> "周日"
                    "월" -> "周一"
                    "화" -> "周二"
                    "수" -> "周三"
                    "목" -> "周四"
                    "금" -> "周五"
                    "토" -> "周六"
                    else -> "-"
                }
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
        return when (Locale.getDefault().language) {
            "ko" -> "${planDayOfMonth}일 ${planDayOfWeek}요일"
            "zh" -> "${planDayOfMonth}日 $planDayOfWeek"
            else -> "$planDayOfMonth $planDayOfWeek"
        }
    }

    fun getMonthFormatBy(month: Int): String {
        return when (Locale.getDefault().language) { // 한글일 때
            "ko" -> "${month}월"
            "zh" -> "${month}月"
            else -> Month.EngMonth.values()[month - 1].name
        }
    }
}