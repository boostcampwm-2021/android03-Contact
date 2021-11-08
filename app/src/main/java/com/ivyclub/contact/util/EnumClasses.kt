package com.ivyclub.contact.util

enum class GroupNameValidation : ValidationMessage{
    WRONG_EMPTY { override val message = "0자 이상 입력해주세요." },
    WRONG_DUPLICATE { override val message = "이미 존재하는 그룹 이름입니다."},
    CORRECT { override val message = "" }
}

interface ValidationMessage {
    val message: String
}

enum class DayOfWeek(val value: Int) : KoreanTranslatable {
    SUN(1) { override val korean = "일" },
    MON(2) { override val korean = "월" },
    TUE(3) { override val korean = "화" },
    WED(4) { override val korean = "수" },
    THU(5) { override val korean = "목" },
    FRI(6) { override val korean = "금" },
    SAT(0) { override val korean = "토" }
}

interface KoreanTranslatable {
    val korean: String
}