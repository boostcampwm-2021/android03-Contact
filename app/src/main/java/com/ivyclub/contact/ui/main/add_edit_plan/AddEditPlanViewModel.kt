package com.ivyclub.contact.ui.main.add_edit_plan

import androidx.lifecycle.*
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.SimpleFriendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class AddEditPlanViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {
    private var planId = -1L
    private val friendMap = mutableMapOf<Long, SimpleFriendData>()
    private val _friendList = MutableLiveData<List<SimpleFriendData>>()
    val friendList: LiveData<List<SimpleFriendData>> = _friendList

    private val loadFriendsJob: Job = viewModelScope.launch(Dispatchers.IO) {
        val myFriends = repository.getSimpleFriendData()
        myFriends?.forEach {
            friendMap[it.id] = it
        }
        _friendList.postValue(myFriends)
    }

    val planTitle = MutableLiveData<String>()

    private val _planTime = MutableLiveData(Date(System.currentTimeMillis()))
    val planTime: LiveData<Date> = _planTime

    private val _planParticipants = MutableLiveData<List<SimpleFriendData>>(emptyList())
    val planParticipants: LiveData<List<SimpleFriendData>> = _planParticipants

    val planPlace = MutableLiveData<String>()
    val planContent = MutableLiveData<String>()

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun getLastPlan(planId: Long) {
        if (this.planId != -1L) return

        this.planId = planId

        viewModelScope.launch(Dispatchers.IO) {
            repository.getPlanDetailsById(planId)?.let {
                planTitle.postValue(it.title)
                _planTime.postValue(it.date)
                planPlace.postValue(it.place)
                planContent.postValue(it.content)

                loadFriendsJob.join()
                val friendsOnPlan = mutableListOf<SimpleFriendData>()
                it.participant.forEach { phoneNumber ->
                    friendMap[phoneNumber]?.let { friendInfo -> friendsOnPlan.add(friendInfo) }
                }
                _planParticipants.postValue(friendsOnPlan)
            }
        }
    }

    fun addParticipant(participantData: SimpleFriendData) {
        val participants = planParticipants.value?.toMutableList()
        participants?.let {
            it.add(participantData)
            _planParticipants.value = it
        }
    }

    fun removeParticipant(index: Int) {
        val participants = planParticipants.value?.toMutableList()
        participants?.let {
            it.removeAt(index)
            _planParticipants.value = it
        }
    }

    fun setNewDate(newDate: Date) {
        _planTime.value = newDate
    }

    fun savePlan(planId: Long) {
        val participants = planParticipants.value?.map { it.id }
    }
}