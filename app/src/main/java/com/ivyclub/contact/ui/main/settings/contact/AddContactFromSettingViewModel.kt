package com.ivyclub.contact.ui.main.settings.contact

import androidx.lifecycle.ViewModel
import com.ivyclub.contact.model.PhoneContactData
import com.ivyclub.contact.util.ContactListManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddContactFromSettingViewModel @Inject constructor(
    private val contactListManager: ContactListManager
) : ViewModel() {
    fun getContactList(): MutableList<PhoneContactData> {
        val contactList = contactListManager.getContact()
        return contactList
    }
}