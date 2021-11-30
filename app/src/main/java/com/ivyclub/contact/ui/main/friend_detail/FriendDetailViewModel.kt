package com.ivyclub.contact.ui.main.friend_detail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.ImageManager
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.PlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FriendDetailViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _friendData = MutableLiveData<FriendData>()
    val friendData: LiveData<FriendData> get() = _friendData
    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> get() = _groupName

    private val _lastPlans = MutableLiveData<List<PlanData>>()
    val lastPlans: LiveData<List<PlanData>> = _lastPlans

    private val _goPlanDetailsEvent = SingleLiveEvent<Long>()
    val goPlanDetailsEvent: LiveData<Long> = _goPlanDetailsEvent

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    fun loadFriendData(id: Long) {
        viewModelScope.launch {
            val friend = repository.getFriendDataById(id)
            _friendData.value = friend
            _groupName.value = repository.getGroupNameById(friend.groupId)
            loadPlans(friend.planList)
        }
    }

    fun setFavorite(id: Long, state: Boolean) {
        viewModelScope.launch {
            repository.setFavorite(id, state)
        }
    }

    private suspend fun loadPlans(planIds: List<Long>) {
        val plans = repository.getPlansByIds(planIds).filter { it.date < Date() }
            .sortedByDescending { it.date }
        _lastPlans.value = plans
    }

    fun deleteFriend(friendId: Long) {
        ImageManager.deleteImage(friendId)
        viewModelScope.launch {
            repository.deleteFriend(friendId)
            finish()
        }
    }

    fun goPlanDetails(planId: Long) {
        if (planId == -1L) return
        _goPlanDetailsEvent.value = planId
    }

    private fun finish() {
        _finishEvent.call()
    }
}