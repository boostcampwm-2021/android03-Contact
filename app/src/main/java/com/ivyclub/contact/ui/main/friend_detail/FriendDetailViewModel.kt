package com.ivyclub.contact.ui.main.friend_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.PlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FriendDetailViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _friendData = MutableLiveData<FriendData>()
    val friendData: LiveData<FriendData> get() = _friendData

    private val _plan1 = MutableLiveData<PlanData>()
    val plan1: LiveData<PlanData> get() = _plan1

    private val _plan2 = MutableLiveData<PlanData>()
    val plan2: LiveData<PlanData> get() = _plan2

    fun loadFriendData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _friendData.postValue(repository.getFriendDataById(id))
        }
    }

    fun setFavorite(id: Long, state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setFavorite(id, state)
        }
    }

    fun loadPlans(planIds: List<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            val plans = repository.getPlansByIds(planIds).filter { it.date < Date() }.sortedByDescending { it.date }
            if(plans.size > 2) {
                _plan1.postValue(plans[0])
                _plan2.postValue(plans[1])
            }
            println(plans)
        }
    }
}