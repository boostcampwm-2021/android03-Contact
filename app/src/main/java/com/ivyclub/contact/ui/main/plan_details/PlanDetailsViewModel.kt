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
            val planData = repository.getPlanDetailsById(planId)
            if (planData != null) {
                val friends = mutableListOf<String>()
                planData.participant.forEach {
                    friends.add(repository.getFriendNameByPhoneNumber(it))
                }
                _planParticipants.postValue(friends)
                _planDetails.postValue(planData)
            } else { // temp
                val tmpPlanData = PlanData(
                    0L,
                    emptyList(),
                    Date(System.currentTimeMillis()),
                    "부스트캠프 모임",
                    "서울시 강남구",
                    "프로젝트 회의, 저녁식사",
                    ""
                )
                val friends = listOf("홍길동", "철수", "영희", "맹구", "태훈")
                _planParticipants.postValue(friends)
                _planDetails.postValue(tmpPlanData)
            }
        }
    }
}