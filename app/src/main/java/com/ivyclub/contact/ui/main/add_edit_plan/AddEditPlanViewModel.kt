package com.ivyclub.contact.ui.main.add_edit_plan

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.ivyclub.contact.R
import com.ivyclub.contact.service.plan_reminder.PlanReminderNotificationWorker
import com.ivyclub.contact.util.SingleLiveEvent
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.image.ImageManager
import com.ivyclub.data.model.PlanData
import com.ivyclub.data.model.SimpleFriendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class AddEditPlanViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val workManager: WorkManager
) : ViewModel() {
    private var planId = -1L
    private val lastParticipants = mutableListOf<Long>()
    private val friendMap = mutableMapOf<Long, SimpleFriendData>()
    private val _friendList = MutableLiveData<List<SimpleFriendData>>()
    val friendList: LiveData<List<SimpleFriendData>> = _friendList
    private val loadFriendsJob: Job = viewModelScope.launch {
        val myFriends = repository.getSimpleFriendData()
        myFriends.forEach {
            friendMap[it.id] = it
        }
        _friendList.value = myFriends
    }
    val planTitle = MutableLiveData<String>()
    private val _planTime = MutableLiveData(Date(System.currentTimeMillis()))
    val planTime: LiveData<Date> = _planTime
    private val _planParticipants = MutableLiveData<List<SimpleFriendData>>(emptyList())
    val planParticipants: LiveData<List<SimpleFriendData>> = _planParticipants
    val planPlace = MutableLiveData<String>()
    val planContent = MutableLiveData<String>()
    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage
    private val _finishEvent = SingleLiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent
    private val _bitmapUriList = MutableLiveData<List<Uri>>() // 계획 사진 uri 리스트
    val bitmapUriList: LiveData<List<Uri>> get() = _bitmapUriList
    private val _imageCount = MutableLiveData(0)
    val imageCount: LiveData<Int> get() = _imageCount
    val maxPhotoCount = MAX_PHOTO_COUNT

    fun getLastPlan(planId: Long) {
        if (this.planId != -1L) return
        this.planId = planId
        viewModelScope.launch {
            repository.getPlanDataById(planId)?.let {
                lastParticipants.addAll(it.participant)

                planTitle.value = it.title
                _planTime.value = it.date
                planPlace.value = it.place
                planContent.value = it.content

                loadFriendsJob.join()
                val friendsOnPlan = mutableListOf<SimpleFriendData>()
                it.participant.forEach { phoneNumber ->
                    friendMap[phoneNumber]?.let { friendInfo -> friendsOnPlan.add(friendInfo) }
                }
                _planParticipants.value = friendsOnPlan
            }
        }
    }

    fun addParticipant(participantData: SimpleFriendData) {
        val participants = planParticipants.value?.toMutableSet()
        participants?.let {
            it.add(participantData)
            _planParticipants.value = trimParticipants(it.toList())
        }
    }

    fun removeParticipant(index: Int) {
        val participants = planParticipants.value?.toMutableList()
        participants?.let {
            it.removeAt(index)
            _planParticipants.value = it
        }
    }

    fun addParticipantsByGroup(groupId: Long) {
        if (groupId == -1L) return

        val participantSet = planParticipants.value?.toMutableSet()
        participantSet?.let { set ->
            viewModelScope.launch {
                val friendsInGroup = repository.getSimpleFriendDataListByGroup(groupId)
                set.addAll(friendsInGroup)
                _planParticipants.value = trimParticipants(set.toList())
            }
        }
    }

    fun setPlanImageUri(newUriList: List<Uri>) {
        val originPlusNewUriList = (_bitmapUriList.value ?: emptyList()) + newUriList
        if (originPlusNewUriList.size > 5) return // 최대 사진 다섯 장
        _imageCount.value = originPlusNewUriList.size
        _bitmapUriList.value = originPlusNewUriList
    }

    private fun trimParticipants(beforeTrimmed: List<SimpleFriendData>): List<SimpleFriendData> {
        var afterTrimmed = beforeTrimmed

        if (beforeTrimmed.size > MAX_PARTICIPANTS) {
            makeSnackbar(R.string.particpants_overload)
            afterTrimmed = beforeTrimmed.subList(0, MAX_PARTICIPANTS)
        }

        return afterTrimmed
    }

    fun setNewDate(newDate: Date) {
        _planTime.value = newDate
    }

    fun savePlan(planImageUriList: List<Bitmap>) {
        val currentTime =
            SimpleDateFormat("yyyyMddhhmmss", Locale.getDefault()).format(java.util.Date())
        val participantIds = planParticipants.value?.map { it.id } ?: emptyList()
        val participantNames = planParticipants.value?.map { it.name } ?: emptyList()
        val planDate = planTime.value ?: Date(System.currentTimeMillis())
        val title = planTitle.value
        if (title.isNullOrEmpty()) {
            makeSnackbar(R.string.hint_plan_title)
            return
        }
        val place = planPlace.value ?: ""
        val content = planContent.value ?: ""
        val color = ""  // TODO: 랜덤 색 만들기
        val newPlan =
            if (planId != -1L) PlanData(
                participantIds,
                planDate,
                title,
                place,
                content,
                color,
                currentTime,
                id = planId
            )
            else PlanData(participantIds, planDate, title, place, content, color, currentTime)
        viewModelScope.launch {
            ImageManager.savePlanBitmap(planImageUriList, currentTime)
            planId = repository.savePlanData(newPlan, lastParticipants)
            val alarmStart = repository.getStartAlarmHour()
            val alarmEnd = repository.getEndAlarmHour()
            PlanReminderNotificationWorker.setPlanAlarm(
                planId, title, participantNames, planDate, alarmStart, alarmEnd, workManager
            )
            makeSnackbar(
                if (planId == -1L) R.string.add_plan_success
                else R.string.update_plan_success
            )
            finish()
        }
    }

    private fun makeSnackbar(strId: Int) {
        _snackbarMessage.value = strId
    }

    fun finish() {
        _finishEvent.call()
    }

    fun addFriend(friendId: Long) {
        viewModelScope.launch {
            val friend = repository.getSimpleFriendDataById(friendId)
            addParticipant(friend)
        }
    }

    fun deletePhotoAt(position: Int) {
        val modifiedPhotoUriList = _bitmapUriList.value?.map { it }?.toMutableList()
        modifiedPhotoUriList?.removeAt(position)
        _imageCount.postValue(modifiedPhotoUriList?.size)
        _bitmapUriList.postValue(modifiedPhotoUriList ?: emptyList())
    }

    companion object {
        const val MAX_PARTICIPANTS = 30
    }
}