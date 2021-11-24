package com.ivyclub.contact.ui.main.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.ui.plan_list.PlanListItemViewModel
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.SimplePlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _planListItems = MutableLiveData<List<PlanListItemViewModel>>()
    val planListItems: LiveData<List<PlanListItemViewModel>> = _planListItems

    private var planListSnapshot = emptyList<SimplePlanData>()

    init { getMyPlans() }

    private fun getMyPlans() {
        viewModelScope.launch {
            _loading.value = true

            repository.loadPlanListWithFlow().buffer()
                .transform { planList ->
                    planListSnapshot = planList
                    emit(planList.mapToPlanItemList(setFriendMap()))
                }.collect { planItemViewModels ->
                    _planListItems.value = planItemViewModels
                    _loading.value = false
                }
        }
    }

    fun refreshPlanItems() {
        val previousItems = planListItems.value
        if (previousItems.isNullOrEmpty()) return

        viewModelScope.launch {
            val newItems = planListSnapshot.mapToPlanItemList(setFriendMap())
            if (previousItems != newItems) {
                _planListItems.value = newItems
            }
        }
    }

    private suspend fun setFriendMap(): Map<Long, String> {
        val friendMap = mutableMapOf<Long, String>()
        repository.getSimpleFriendData()?.forEach {
            friendMap[it.id] = it.name
        }
        return friendMap
    }

    private fun List<SimplePlanData>.mapToPlanItemList(friendMap: Map<Long, String>)
    : List<PlanListItemViewModel> {
        val planItems = mutableListOf<PlanListItemViewModel>()

        forEach { planData ->
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

        return planItems
    }
}