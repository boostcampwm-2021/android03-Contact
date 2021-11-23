package com.ivyclub.contact.ui.main.settings.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.ivyclub.contact.service.plan_reminder.PlanReminderNotificationWorker
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationTimeDialogViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val friendMap = mutableMapOf<Long, String>()
    private val loadFriendsJob: Job = viewModelScope.launch {
        repository.getSimpleFriendData()?.forEach {
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
            repository.getPlanListAfter(System.currentTimeMillis())?.forEach { simplePlanData ->
                val friendList = mutableListOf<String>()
                simplePlanData.participant.forEach { friendId ->
                    friendMap[friendId]?.let { friendList.add(it) }
                }

                PlanReminderNotificationWorker
                    .resetDayStartEndAlarms(
                        simplePlanData.id,
                        startTime.toInt(),
                        endTime.toInt(),
                        simplePlanData.date,
                        friendList,
                        workManager
                    )

                _changeNotiTimeFinishEvent.call()
            }
        }
    }
}