package com.ivyclub.contact.ui.main.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.ui.plan_list.PlanListItemViewModel
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _planListItems = MutableLiveData<List<PlanListItemViewModel>>()
    val planListItems: LiveData<List<PlanListItemViewModel>> = _planListItems

    private val friendMap = mutableMapOf<Long, String>()

    private val loadFriendsJob: Job = viewModelScope.launch(Dispatchers.IO) {
        val myFriends = repository.getSimpleFriendData()
        myFriends?.forEach {
            friendMap[it.id] = it.name
        }
    }

    fun getMyPlans() {
        viewModelScope.launch(Dispatchers.IO) {
            loadFriendsJob.join()

            val myPlanList = repository.getPlanList()
            val planItems = mutableListOf<PlanListItemViewModel>()

            myPlanList?.forEach { planData ->
                val friends = mutableListOf<String>()
                planData.participant.forEach { friendId ->
                    friendMap[friendId]?.let { friendName ->
                        friends.add(friendName)
                    }
                }
                planItems.add(
                    PlanListItemViewModel(planData, friends)
                )
            }

            _planListItems.postValue(planItems)
        }
    }
}