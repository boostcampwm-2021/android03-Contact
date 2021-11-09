package com.ivyclub.contact.ui.main.add_friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(val repository: ContactRepository) : ViewModel() {

    private val _groups = MutableLiveData<List<String>>()
    val groups: LiveData<List<String>> get() = _groups

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val groupList = repository.loadGroups().map { it.name }
            _groups.postValue(groupList)
        }
    }
}