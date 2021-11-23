package com.ivyclub.contact.util

sealed class ContactSavingUiState {
    object Loading : ContactSavingUiState()
    object LoadingDone : ContactSavingUiState()
    object Empty : ContactSavingUiState()
}