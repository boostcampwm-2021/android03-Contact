package com.ivyclub.contact.ui.main.settings.contact

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.contact.model.PhoneContactData
import com.ivyclub.contact.util.ContactListManager
import com.ivyclub.contact.util.ContactSavingUiState
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactFromSettingViewModel @Inject constructor(
    private val contactListManager: ContactListManager,
    private val repository: ContactRepository
) : ViewModel() {

    private val _loadingUiState = MutableStateFlow<ContactSavingUiState>(ContactSavingUiState.Empty)
    val loadingUIState = _loadingUiState.asStateFlow()
    val contactList = mutableListOf<PhoneContactData>()

    init {
        getContactList()
    }

    private fun getContactList() {
        val phoneContactList = contactListManager.getContact()
        viewModelScope.launch {
            _loadingUiState.value = ContactSavingUiState.Loading
            val originFriendsList = repository.loadFriends()
            phoneContactList.forEach { contact ->
                if (!originFriendsList.any { it.phoneNumber == contact.phoneNumber }) {
                    contactList.add(contact)
                }
            }
            _loadingUiState.value = ContactSavingUiState.LoadingDone
        }
    }
}