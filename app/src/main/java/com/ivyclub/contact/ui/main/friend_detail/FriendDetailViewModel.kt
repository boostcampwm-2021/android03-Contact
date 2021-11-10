package com.ivyclub.contact.ui.main.friend_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.FriendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendDetailViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _friendData = MutableLiveData<FriendData>()
    val friendData: LiveData<FriendData> get() = _friendData

    fun loadFriendData(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _friendData.postValue(repository.getFriendDataById(id))
        }
    }

    fun setFavorite(id: Long, state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setFavorite(id, state)
        }
    }
}