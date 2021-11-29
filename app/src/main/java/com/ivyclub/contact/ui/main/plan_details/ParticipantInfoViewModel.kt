package com.ivyclub.contact.ui.main.plan_details

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.ImageManager
import com.ivyclub.data.model.FriendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ParticipantInfoViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _participantData = MutableLiveData<FriendData>()
    val participantData: LiveData<FriendData> = _participantData

    private val _participantGroup = MutableLiveData<String>()
    val participantGroup: LiveData<String> = _participantGroup

    private val _participantImage = MutableLiveData<Bitmap?>()
    val participantImage: LiveData<Bitmap?> = _participantImage

    fun getParticipantData(participantId: Long) {
        if (participantId == -1L) return

        viewModelScope.launch {
            val data = repository.getFriendDataById(participantId)
            _participantData.value = data
            val groupName = repository.getGroupNameById(data.groupId)
            _participantGroup.value = groupName
            _participantImage.value = getParticipantImage(participantId)
        }
    }

    private suspend fun getParticipantImage(participantId: Long) =
        withContext(Dispatchers.IO) {
            ImageManager.loadProfileImage(participantId)
        }

    fun getParticipantPhone(): String? {
        val data = participantData.value ?: return null
        return if (data.phoneNumber.isEmpty()) null else data.phoneNumber
    }
}