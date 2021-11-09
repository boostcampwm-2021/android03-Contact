package com.ivyclub.contact.ui.main.friend_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendDetailViewModel @Inject constructor(
    private val repository: ContactRepository
): ViewModel() {

    fun setFavorite(phoneNumber: String, state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setFavorite(phoneNumber, state)
        }
    }
}