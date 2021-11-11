package com.ivyclub.contact.ui.main.plan_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.R
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _planDetails = MutableLiveData<PlanData>()
    val planDetails: LiveData<PlanData> = _planDetails

    private val _planParticipants = MutableLiveData<List<String>>()
    val planParticipants: LiveData<List<String>> = _planParticipants

    private val _toastMessage = SingleLiveEvent<Int>()
    val toastMessage: LiveData<Int> = _toastMessage

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

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

    fun deletePlan() {
        val planData = planDetails.value ?: return

        viewModelScope.launch(Dispatchers.IO) {
            //repository.deletePlanData(planData)
            makeToast(R.string.delete_plan_success)
            finish()
        }
    }

    private fun makeToast(strId: Int) {
        _toastMessage.postValue(strId)
    }

    fun finish() {
        _finishEvent.call()
    }
}