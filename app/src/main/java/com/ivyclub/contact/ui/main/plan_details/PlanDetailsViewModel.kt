package com.ivyclub.contact.ui.main.plan_details

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.R
import com.ivyclub.contact.service.plan_reminder.PlanReminderMaker
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.image.ImageManager
import com.ivyclub.data.image.ImageType
import com.ivyclub.data.model.PlanData
import com.ivyclub.data.model.SimpleFriendData
import com.ivyclub.data.model.SimplePlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val reminderMaker: PlanReminderMaker
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

    private val _folderExists = MutableLiveData<Boolean>()
    val folderExists: LiveData<Boolean> get() = _folderExists

    private val _photoIds = MutableLiveData<List<String>>(emptyList())
    val photoIds: LiveData<List<String>> get() = _photoIds

    private val friendMap = mutableMapOf<Long, SimpleFriendData>()

    private val loadFriendsJob: Job = viewModelScope.launch {
        val myFriends = repository.getSimpleFriendData()
        myFriends.forEach {
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

            checkFolderExists(planData.id)

            if (removedFriendsIds.isNotEmpty()) {
                repository.updatePlansParticipants(planData.participant - removedFriendsIds, planId)
            }
        }
    }

    private fun checkFolderExists(planId: Long) {
        val folderPath = "${ImageType.PLAN_IMAGE.filePath}${planId}/"
        val file = File(folderPath)
        _folderExists.value = file.exists()
    }

    fun getPhotos(planId: Long) {
        val folderPath = "${ImageType.PLAN_IMAGE.filePath}${planId}/"
        val file = File(folderPath)
        val photos = mutableListOf<String>()
        file.walk().forEach {
            if (it.name.endsWith("jpg")) photos.add(it.name)
        }
        _photoIds.value = photos
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

        if (phoneNumbers.isEmpty()) {
            _snackbarMessage.value = R.string.no_participants_numbers
            return
        }

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
            ImageManager.deletePlanImageFolder(planData.id.toString())
            repository.deletePlanData(planData)
            reminderMaker.cancelPlanReminder(
                SimplePlanData(planData.id, planData.title, planData.date, planData.participant)
            )
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