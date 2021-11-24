package com.ivyclub.contact.ui.main.plan_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.ivyclub.contact.R
import com.ivyclub.contact.service.plan_reminder.PlanReminderNotificationWorker
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val _planDetails = MutableLiveData<PlanData>()
    val planDetails: LiveData<PlanData> = _planDetails

    private val _planParticipants = MutableLiveData<List<String>>()
    val planParticipants: LiveData<List<String>> = _planParticipants

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    private val friendMap = mutableMapOf<Long, String>()

    private val loadFriendsJob: Job = viewModelScope.launch {
        val myFriends = repository.getSimpleFriendData()
        myFriends?.forEach {
            friendMap[it.id] = it.name
        }
    }

    fun getPlanDetails(planId: Long) {
        viewModelScope.launch {
            val planData = repository.getPlanDataById(planId)
            val removedFriendsIds = mutableListOf<Long>()

            loadFriendsJob.join()
            val friends = mutableListOf<String>()
            planData.participant.forEach { friendId ->
                val friendName = friendMap[friendId]
                if (friendName == null) removedFriendsIds.add(friendId)
                else friends.add(friendName)
            }

            _planParticipants.value = friends
            _planDetails.value = planData

            if (removedFriendsIds.isNotEmpty()) {
                repository.updatePlansParticipants(planData.participant - removedFriendsIds, planId)
            }
        }
    }

    fun deletePlan() {
        val planData = planDetails.value ?: return

        viewModelScope.launch {
            repository.deletePlanData(planData)
            PlanReminderNotificationWorker.cancelPlanAlarms(planData.id, workManager)
            makeSnackbar(R.string.delete_plan_success)
            finish()
        }
    }

    private fun makeSnackbar(strId: Int) {
        _snackbarMessage.value = strId
    }

    private fun finish() {
        _finishEvent.call()
    }
}