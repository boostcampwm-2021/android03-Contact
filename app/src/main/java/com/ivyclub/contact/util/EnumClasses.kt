package com.ivyclub.contact.util

enum class DayOfWeek(val value: Int) : KoreanTranslatable {
    SUN(1) {
        override val korean = StringManager.getString("일")
    },
    MON(2) {
        override val korean = StringManager.getString("월")
    },
    TUE(3) {
        override val korean = StringManager.getString("화")
    },
    WED(4) {
        override val korean = StringManager.getString("수")
    },
    THU(5) {
        override val korean = StringManager.getString("목")
    },
    FRI(6) {
        override val korean = StringManager.getString("금")
    },
    SAT(0) {
        override val korean = StringManager.getString("토")
    }
}

interface KoreanTranslatable {
    val korean: String
}

enum class FriendListViewType {
    GROUP_NAME, FRIEND, GROUP_DIVIDER
}

enum class PasswordViewType {
    SET_PASSWORD, RECONFIRM_PASSWORD, APP_CONFIRM_PASSWORD, SECURITY_CONFIRM_PASSWORD
}

enum class NotificationType(val value: String) {
    PLAN("PLAN"), MORNING("MORNING"), NIGHT("NIGHT")
}

enum class Month {
    ;
    enum class EngMonth {
        Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
    }
}