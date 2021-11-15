package com.ivyclub.contact.ui.main.friend_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.ui.plan_list.PlanListItemViewModel
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.SimplePlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendAllPlanViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {
    private val _planListItems = MutableLiveData<List<PlanListItemViewModel>>()
    val planListItems: LiveData<List<PlanListItemViewModel>> = _planListItems

    private val friendMap = mutableMapOf<Long, String>()

    private val loadFriendsJob: Job = viewModelScope.launch {
        val myFriends = repository.getSimpleFriendData()
        myFriends?.forEach {
            friendMap[it.id] = it.name
        }
    }

    fun getMyPlans(plans: LongArray) {
        viewModelScope.launch {
            loadFriendsJob.join()

            val myPlanList = repository.getPlansByIds(plans.toList()).sortedBy { it.date }
            val planItems = mutableListOf<PlanListItemViewModel>()

            myPlanList?.forEach { planData ->
                val friends = mutableListOf<String>()
                planData.participant.forEach { friendId ->
                    friendMap[friendId]?.let { friendName ->
                        friends.add(friendName)
                    }
                }
                planItems.add(
                    PlanListItemViewModel(
                        SimplePlanData(
                            planData.id,
                            planData.title,
                            planData.date,
                            planData.participant
                        ), friends
                    )
                )

            }

            _planListItems.value = planItems
        }
    }
}