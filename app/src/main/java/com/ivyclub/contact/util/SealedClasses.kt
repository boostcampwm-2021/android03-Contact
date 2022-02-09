package com.ivyclub.contact.util

sealed class ContactSavingUiState {
    object Loading : ContactSavingUiState()
    object LoadingDone : ContactSavingUiState()
    object Dialog : ContactSavingUiState()
    object DialogDone : ContactSavingUiState()
    object Empty : ContactSavingUiState()
}