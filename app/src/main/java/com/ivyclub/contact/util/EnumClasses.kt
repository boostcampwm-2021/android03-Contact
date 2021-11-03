package com.ivyclub.contact.util

enum class DayOfWeek(val value: Int) : KoreanTranslatable {
    SUN(1) { override val korean = "일요일" },
    MON(2) { override val korean = "월요일" },
    TUE(3) { override val korean = "화요일" },
    WED(4) { override val korean = "수요일" },
    THU(5) { override val korean = "목요일" },
    FRI(6) { override val korean = "금요일" },
    SAT(0) { override val korean = "토요일" }
}

interface KoreanTranslatable {
    val korean: String
}