package com.ivyclub.contact.ui.main.add_edit_plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.R
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PlanData
import com.ivyclub.data.model.SimpleFriendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject
import kotlin.collections.List
import kotlin.collections.emptyList
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toList
import kotlin.collections.toMutableList
import kotlin.collections.toMutableSet

@HiltViewModel
class AddEditPlanViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {
    private var planId = -1L
    private val lastParticipants = mutableListOf<Long>()
    private val friendMap = mutableMapOf<Long, SimpleFriendData>()
    private val _friendList = MutableLiveData<List<SimpleFriendData>>()
    val friendList: LiveData<List<SimpleFriendData>> = _friendList

    private val loadFriendsJob: Job = viewModelScope.launch {
        val myFriends = repository.getSimpleFriendData()
        myFriends?.forEach {
            friendMap[it.id] = it
        }
        _friendList.value = myFriends
    }

    val planTitle = MutableLiveData<String>()

    private val _planTime = MutableLiveData(Date(System.currentTimeMillis()))
    val planTime: LiveData<Date> = _planTime

    private val _planParticipants = MutableLiveData<List<SimpleFriendData>>(emptyList())
    val planParticipants: LiveData<List<SimpleFriendData>> = _planParticipants

    val planPlace = MutableLiveData<String>()
    val planContent = MutableLiveData<String>()

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    fun getLastPlan(planId: Long) {
        if (this.planId != -1L) return

        this.planId = planId

        viewModelScope.launch {
            repository.getPlanDataById(planId)?.let {
                lastParticipants.addAll(it.participant)

                planTitle.value = it.title
                _planTime.value = it.date
                planPlace.value = it.place
                planContent.value = it.content

                loadFriendsJob.join()
                val friendsOnPlan = mutableListOf<SimpleFriendData>()
                it.participant.forEach { phoneNumber ->
                    friendMap[phoneNumber]?.let { friendInfo -> friendsOnPlan.add(friendInfo) }
                }
                _planParticipants.value = friendsOnPlan
            }
        }
    }

    fun addParticipant(participantData: SimpleFriendData) {
        val participants = planParticipants.value?.toMutableSet()
        participants?.let {
            it.add(participantData)
            _planParticipants.value = it.toList()
        }
    }

    fun removeParticipant(index: Int) {
        val participants = planParticipants.value?.toMutableList()
        participants?.let {
            it.removeAt(index)
            _planParticipants.value = it
        }
    }

    fun addParticipantsByGroup(groupName: String) {
        val participantSet = planParticipants.value?.toMutableSet()
        participantSet?.let { set ->
            viewModelScope.launch {
                val friendsInGroup = repository.getSimpleFriendDataListByGroup(groupName)
                set.addAll(friendsInGroup)
                _planParticipants.value = set.toList()
            }
        }
    }

    fun setNewDate(newDate: Date) {
        _planTime.value = newDate
    }

    fun savePlan() {
        val participants = planParticipants.value?.map { it.id } ?: emptyList()
        val planDate = planTime.value ?: Date(System.currentTimeMillis())

        val title = planTitle.value
        if (title.isNullOrEmpty()) {
            makeSnackbar(R.string.hint_plan_title)
            return
        }

        val place = planPlace.value ?: ""
        val content = planContent.value ?: ""
        val color = ""  // TODO: 랜덤 색 만들기

        val newPlan =
            if (planId != -1L) PlanData(participants, planDate, title, place, content, color, planId)
            else PlanData(participants, planDate, title, place, content, color)

        viewModelScope.launch {
            repository.savePlanData(newPlan, lastParticipants)
            makeSnackbar(
                if (planId == -1L) R.string.add_plan_success
                else R.string.update_plan_success
            )
            finish()
        }
    }

    private fun makeSnackbar(strId: Int) {
        _snackbarMessage.value = strId
    }

    fun finish() {
        _finishEvent.call()
    }
}