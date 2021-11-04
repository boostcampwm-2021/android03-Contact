package com.ivyclub.contact.ui.onboard.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PersonData
import com.ivyclub.data.model.PhoneContactData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    fun savePeople(data: List<PhoneContactData>) {
        viewModelScope.launch(Dispatchers.IO) {
            data.forEach {
                repository.savePeople(
                    PersonData(
                        it.phoneNumber,
                        it.name,
                        "",
                        "friend",
                        listOf(),
                        false,
                        mapOf()
                    )
                )
            }
        }
    }

}