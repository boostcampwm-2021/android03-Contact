package com.ivyclub.contact.ui.main.plan_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.ivyclub.contact.R
import com.ivyclub.contact.service.PlanReminderNotificationWorker
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PlanData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val _planDetails = MutableLiveData<PlanData>()
    val planDetails: LiveData<PlanData> = _planDetails

    private val _planParticipants = MutableLiveData<List<String>>()
    val planParticipants: LiveData<List<String>> = _planParticipants

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    fun getPlanDetails(planId: Long) {
        viewModelScope.launch {
            val planData = repository.getPlanDataById(planId)

            val friends = mutableListOf<String>()
            planData.participant.forEach {
                friends.add(repository.getSimpleFriendDataById(it).name)
            }
            _planParticipants.value = friends
            _planDetails.value = planData
        }
    }

    fun deletePlan() {
        val planData = planDetails.value ?: return

        viewModelScope.launch {
            repository.deletePlanData(planData)
            PlanReminderNotificationWorker.cancelPlanAlarms(planData.id, workManager)
            makeSnackbar(R.string.delete_plan_success)
            finish()
        }
    }

    private fun makeSnackbar(strId: Int) {
        _snackbarMessage.value = strId
    }

    private fun finish() {
        _finishEvent.call()
    }
}