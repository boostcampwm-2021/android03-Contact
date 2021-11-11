package com.ivyclub.contact.ui.main.plan_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _planDetails = MutableLiveData<PlanData>()
    val planDetails: LiveData<PlanData> = _planDetails

    private val _planParticipants = MutableLiveData<List<String>>()
    val planParticipants: LiveData<List<String>> = _planParticipants

    fun getPlanDetails(planId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val planData = repository.getPlanDataById(planId)
            if (planData != null) {
                val friends = mutableListOf<String>()
                planData.participant.forEach {
                    friends.add(repository.getSimpleFriendDataById(it).name)
                }
                _planParticipants.postValue(friends)
                _planDetails.postValue(planData)
            }
        }
    }
}