package com.ivyclub.contact.ui.main.friend_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
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

    private val _plan1 = MutableLiveData<PlanData>()
    val plan1: LiveData<PlanData> get() = _plan1
    private val _plan1Exist = MutableLiveData<Boolean>()
    val plan1Exist: LiveData<Boolean> get() = _plan1Exist
    private val _plan1Date = MutableLiveData<Date>()
    val plan1Date: LiveData<Date> get() = _plan1Date

    private val _plan2 = MutableLiveData<PlanData>()
    val plan2: LiveData<PlanData> get() = _plan2
    private val _plan2Exist = MutableLiveData<Boolean>()
    val plan2Exist: LiveData<Boolean> get() = _plan2Exist
    private val _plan2Date = MutableLiveData<Date>()
    val plan2Date: LiveData<Date> get() = _plan2Date

    fun loadFriendData(id: Long) {
        viewModelScope.launch {
            _friendData.value = repository.getFriendDataById(id)
        }
    }

    fun setFavorite(id: Long, state: Boolean) {
        viewModelScope.launch {
            repository.setFavorite(id, state)
        }
    }

    fun loadPlans(planIds: List<Long>) {
        viewModelScope.launch {
            val plans = repository.getPlansByIds(planIds).filter { it.date < Date() }
                .sortedByDescending { it.date }
            when {
                plans.size > 1 -> {
                    _plan1.value = plans[0]
                    _plan1Exist.value = true
                    _plan1Date.value = plans[0].date

                    _plan2.value = plans[1]
                    _plan2Exist.value = true
                    _plan2Date.value = plans[1].date
                }
                plans.size == 1 -> {
                    _plan1.value = plans[0]
                    _plan1Exist.value = true
                    _plan1Date.value = plans[0].date
                }
            }
        }
    }
}