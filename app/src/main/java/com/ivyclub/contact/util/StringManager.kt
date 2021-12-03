package com.ivyclub.contact.util

import java.util.*

object StringManager {
    fun getString(targetString: String): String {
        when (Locale.getDefault().language) {
            "kr" -> { // 한국어일 때
                return targetString
            }
            else -> { // 영어이거나 지원되지 않는 언어일 때
                return when (targetString) {
                    "친구" -> "friend"
                    "즐겨찾기" -> "favorite"
                    "일" -> "Sunday"
                    "월" -> "Monday"
                    "화" -> "Tuesday"
                    "수" -> "Wednesday"
                    "목" -> "Thursday"
                    "금" -> "Friday"
                    "토" -> "Saturday"
                    else -> "-"
                }
            }
        }
    }
}