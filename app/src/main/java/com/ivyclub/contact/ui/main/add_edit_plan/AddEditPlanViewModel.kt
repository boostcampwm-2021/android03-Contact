package com.ivyclub.contact.ui.main.add_edit_plan

import androidx.lifecycle.ViewModel
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditPlanViewModel @Inject constructor(
    repository: ContactRepository
) : ViewModel() {
}