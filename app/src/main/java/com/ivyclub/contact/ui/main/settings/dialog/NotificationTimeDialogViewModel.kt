package com.ivyclub.contact.ui.main.settings.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

    private val _changeNotiTimeFinishEvent = SingleLiveEvent<Unit>()
    val changeNotiTimeFinishEvent: LiveData<Unit> = _changeNotiTimeFinishEvent

    fun updateNotificationTime(startTime: Float, endTime: Float) {
        viewModelScope.launch {
            repository.setNotificationTime(startTime.toInt(), endTime.toInt())
            loadFriendsJob.join()
            val futurePlanList = repository.getPlanListAfter(System.currentTimeMillis())
            if (futurePlanList.isNullOrEmpty()) {
                _changeNotiTimeFinishEvent.call()
                return@launch
            }
            futurePlanList.forEach { simplePlanData ->
                reminderMaker.resetStartEndAlarm(simplePlanData)
            }

            _changeNotiTimeFinishEvent.call()
        }
    }
}