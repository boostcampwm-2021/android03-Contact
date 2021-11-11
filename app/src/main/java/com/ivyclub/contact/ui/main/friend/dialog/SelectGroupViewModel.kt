package com.ivyclub.contact.ui.main.friend.dialog

import androidx.lifecycle.*
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectGroupViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _groupNameList = MutableLiveData<List<String>>()
    val groupNameList: LiveData<List<String>> get() = _groupNameList

    init {
        getGroupNameListFromDatabase()
    }

    private fun getGroupNameListFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            _groupNameList.postValue(repository.loadGroups().map { it.name })
        }
    }
}