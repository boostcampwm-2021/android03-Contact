package com.ivyclub.contact.util

import java.util.*

object StringManager {
    fun getString(targetString: String): String {
        when (Locale.getDefault().language) {
            "en" -> { // 영어일 때
                return when (targetString) {
                    "친구" -> "friend"
                    else -> "favorite"
                }
            }
            "kr" -> { // 한국어일 때
                return when (targetString) {
                    "친구" -> "친구"
                    else -> "즐겨찾기"
                }
            }
            else -> { // 지원되지 않는 언어일 때
                return "-"
            }
        }
    }
}