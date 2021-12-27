package com.ivyclub.contact.ui.main.settings.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker
import com.ivyclub.contact.util.HOUR_IN_MILLIS
import com.ivyclub.contact.util.MINUTE_IN_MILLIS
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.contact.util.getNewTime
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class NotificationTimeDialogViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val reminderMaker: PlanReminderMaker
) : ViewModel() {

    private val friendMap = mutableMapOf<Long, String>()
    private val loadFriendsJob: Job = viewModelScope.launch {
        repository.getSimpleFriendData().forEach {
            friendMap[it.id] = it.name
        }
    }

    val notificationStartTime: Float by lazy {
        repository.getStartAlarmHour().toFloat()
    }
    val notificationEndTime: Float by lazy {
        repository.getEndAlarmHour().toFloat()
    }

    private val planNotiTimes = arrayOf(15 * MINUTE_IN_MILLIS, 30 * MINUTE_IN_MILLIS, HOUR_IN_MILLIS, 2 * HOUR_IN_MILLIS)
    private val _planNotiTimeIdx = MutableLiveData<Int>()
    val planNotiTimeIdx: LiveData<Int> = _planNotiTimeIdx

    private val _changeNotiTimeFinishEvent = SingleLiveEvent<Unit>()
    val changeNotiTimeFinishEvent: LiveData<Unit> = _changeNotiTimeFinishEvent

    init {
        initPlanNotiTime()
    }

    private fun initPlanNotiTime() {
        var planNotiTime = repository.getPlanNotificationTime()
        if (planNotiTime == 0L) {
            planNotiTime = planNotiTimes[2]
            repository.setPlanNotificationTime(planNotiTime)
        }
        planNotiTimes.forEachIndexed { i, time ->
            if (time == planNotiTime)
                _planNotiTimeIdx.value = i
        }
    }

    fun updatePlanNotiIdx(idx: Int) {
        _planNotiTimeIdx.value = idx
    }

    fun updateNotificationTime(startTime: Float, endTime: Float) {
        viewModelScope.launch {
            repository.setNotificationTimeRange(startTime.toInt(), endTime.toInt())
            planNotiTimeIdx.value?.let { repository.setPlanNotificationTime(planNotiTimes[it]) }
            loadFriendsJob.join()
            val todayStart = Date(System.currentTimeMillis()).getNewTime(0, 0).time
            val futurePlanList = repository.getPlanListAfter(todayStart)
            if (futurePlanList.isNullOrEmpty()) {
                _changeNotiTimeFinishEvent.call()
                return@launch
            }
            futurePlanList.forEach { simplePlanData ->
                reminderMaker.makePlanReminders(simplePlanData)
            }

            _changeNotiTimeFinishEvent.call()
        }
    }
}