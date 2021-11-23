package com.ivyclub.contact.ui.main.settings.contact

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddContactFromSettingsBinding
import com.ivyclub.contact.ui.onboard.contact.ContactAdapter
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.ContactSavingUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactFromSettingsFragment :
    BaseFragment<FragmentAddContactFromSettingsBinding>(R.layout.fragment_add_contact_from_settings) {

    private val viewModel: AddContactFromSettingViewModel by viewModels()
    private val contactAdapter by lazy { ContactAdapter() }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            //loadContact()
        } else {
            activity?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        initAdapter()
        observeLoadingUiState()
    }

    private fun initButtons() {
        binding.ivBackIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        binding.rvContactList.adapter = contactAdapter
    }

    private fun observeLoadingUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingUIState.collect { newState ->
                    if (newState == ContactSavingUiState.LoadingDone) {
                        contactAdapter.submitList(viewModel.contactList)
                    }
                }
            }
        }
    }
}