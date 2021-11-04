package com.ivyclub.contact.ui.main.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivyclub.contact.ui.plan_list.PlanListItemViewModel
import com.ivyclub.contact.util.DAY_IN_MILLIS
import com.ivyclub.data.model.AppointmentData
import java.sql.Date
import java.util.*

class PlanViewModel : ViewModel() {

    private val _planListItems = MutableLiveData<List<PlanListItemViewModel>>()
    val planListItems: LiveData<List<PlanListItemViewModel>> = _planListItems

    fun getMyPlans() {
        // temp
        val plans = mutableListOf<PlanListItemViewModel>()

        val startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.MONTH, 6)
        startCalendar.set(Calendar.DAY_OF_MONTH, 19)
        val participants = listOf("홍길동", "철수", "영희")
        repeat(30) {
            val date = Date(startCalendar.timeInMillis + (DAY_IN_MILLIS * 10 * it))
            plans.add(
                PlanListItemViewModel(
                    AppointmentData(
                        it.toLong(),
                        participants,
                        date,
                        "부캠 나들이",
                        "ㅋㅋㅋ",
                        ""
                    )
                )
            )
        }

        _planListItems.value = plans
    }
}