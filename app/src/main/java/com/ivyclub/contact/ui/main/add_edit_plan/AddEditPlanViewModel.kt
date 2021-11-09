package com.ivyclub.contact.ui.main.add_edit_plan

import androidx.lifecycle.*
import com.ivyclub.data.ContactRepository
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
    private val friendMap = mutableMapOf<String, String>()
    private val _friendList = MutableLiveData<List<Pair<String, String>>>()
    val friendList: LiveData<List<Pair<String, String>>> = _friendList

    private val loadFriendsJob: Job = viewModelScope.launch(Dispatchers.IO) {
        val friends = repository.loadFriends()
        friends?.forEach {
            friendMap[it.phoneNumber] = it.name
        }
        _friendList.postValue(friendMap.toList())
    }

    val planTitle = MutableLiveData<String>()

    private val _planTime = MutableLiveData(Date(System.currentTimeMillis()))
    val planTime: LiveData<Date> = _planTime

    private val _planParticipants = MutableLiveData<List<Pair<String, String>>>(emptyList())
    val planParticipants: LiveData<List<Pair<String, String>>> = _planParticipants

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
                val names = mutableListOf<Pair<String, String>>()
                it.participant.forEach { phoneNumber ->
                    friendMap[phoneNumber]?.let { name -> names.add(Pair(phoneNumber, name)) }
                }
                _planParticipants.postValue(names)
            }
        }
    }

    fun addParticipant(participantInfo: Pair<String, String>) {
        val participants = planParticipants.value?.toMutableList()
        participants?.let {
            it.add(participantInfo)
            _planParticipants.value = it
        }
    }

    fun removeParticipant(index: Int) {
        val participants = planParticipants.value?.toMutableList()
        participants?.let {
            val participantToBeRemoved = it[index]
            it.remove(participantToBeRemoved)
            _planParticipants.value = it
        }
    }

    fun setNewDate(newDate: Date) {
        _planTime.value = newDate
    }

    fun savePlan(planId: Long) {
        val participants = planParticipants.value?.map { it.first }
    }
}