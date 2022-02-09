package com.ivyclub.contact.ui.onboard.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.model.PhoneContactData
import com.ivyclub.contact.util.ContactListManager
import com.ivyclub.contact.util.ContactSavingUiState
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.FriendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val contactListManager: ContactListManager
) : ViewModel() {

    private val _isSavingDone = MutableStateFlow<ContactSavingUiState>(ContactSavingUiState.Empty)
    val isSavingDone = _isSavingDone.asStateFlow()

    fun saveFriendsData(data: List<PhoneContactData>) {
        if (data.isEmpty()) {
            _isSavingDone.value = ContactSavingUiState.LoadingDone
        }
        viewModelScope.launch {
            _isSavingDone.value = ContactSavingUiState.Loading
            data.forEach {
                repository.saveFriend(
                    FriendData(
                        it.phoneNumber,
                        it.name,
                        "",
                        1,
                        listOf(),
                        false,
                        mapOf()
                    )
                )
            }
            _isSavingDone.value = ContactSavingUiState.LoadingDone
        }
    }

    fun getContactList(): MutableList<PhoneContactData> {
        return contactListManager.getContact()
    }
}