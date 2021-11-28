package com.ivyclub.contact.ui.main.plan_details

import android.os.Bundle
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
import com.ivyclub.data.model.SimpleFriendData
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

    private val _planParticipants = MutableLiveData<List<SimpleFriendData>>()
    val planParticipants: LiveData<List<SimpleFriendData>> = _planParticipants

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    private val _goFriendDetailsEvent = SingleLiveEvent<Long>()
    val goFriendDetailsEvent: LiveData<Long> = _goFriendDetailsEvent

    private val _sendMessagesToParticipantsEvent = SingleLiveEvent<Bundle>()
    val sendMessagesToParticipantsEvent: LiveData<Bundle> = _sendMessagesToParticipantsEvent

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    private val friendMap = mutableMapOf<Long, SimpleFriendData>()

    private val loadFriendsJob: Job = viewModelScope.launch {
        val myFriends = repository.getSimpleFriendData()
        myFriends?.forEach {
            friendMap[it.id] = it
        }
    }

    fun getPlanDetails(planId: Long) {
        viewModelScope.launch {
            val planData = repository.getPlanDataById(planId)
            val removedFriendsIds = mutableListOf<Long>()

            loadFriendsJob.join()
            val friends = mutableListOf<SimpleFriendData>()
            planData.participant.forEach { friendId ->
                val friendData = friendMap[friendId]
                if (friendData == null) removedFriendsIds.add(friendId)
                else friends.add(friendData)
            }

            _planParticipants.value = friends
            _planDetails.value = planData

            if (removedFriendsIds.isNotEmpty()) {
                repository.updatePlansParticipants(planData.participant - removedFriendsIds, planId)
            }
        }
    }

    fun goParticipantsDetails(index: Int) {
        val participants = _planParticipants.value ?: return
        _goFriendDetailsEvent.value = participants[index].id
    }

    fun sendMessagesToPlanParticipants() {
        val participants = planParticipants.value ?: return
        val planData = planDetails.value ?: return

        val bundle = Bundle()

        val phoneNumbers =
            participants
                .filter { it.phoneNumber.isNotEmpty() }
                .map { it.phoneNumber }

        val strReceivers = "smsto:" + phoneNumbers.reduce { acc, s -> "$acc;$s" }

        bundle.putString(KEY_PHONE_NUMBERS, strReceivers)
        bundle.putString(KEY_PLAN_TITLE, planData.title)
        bundle.putLong(KEY_PLAN_TIME, planData.date.time)
        if (planData.place.isNotEmpty()) bundle.putString(KEY_PLAN_PLACE, planData.place)
        if (planData.content.isNotEmpty()) bundle.putString(KEY_PLAN_CONTENT, planData.content)

        _sendMessagesToParticipantsEvent.value = bundle
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

    companion object {
        const val KEY_PHONE_NUMBERS = "phone_numbers"
        const val KEY_PLAN_TITLE = "plan_title"
        const val KEY_PLAN_TIME = "plan_time"
        const val KEY_PLAN_PLACE = "plan_place"
        const val KEY_PLAN_CONTENT = "plan_content"
    }
}