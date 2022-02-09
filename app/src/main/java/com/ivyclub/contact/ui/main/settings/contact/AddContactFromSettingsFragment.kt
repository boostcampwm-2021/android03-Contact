package com.ivyclub.contact.ui.main.settings.contact

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddContactFromSettingsBinding
import com.ivyclub.contact.ui.main.friend.dialog.SelectGroupFragment
import com.ivyclub.contact.ui.onboard.contact.ContactAdapter
import com.ivyclub.contact.ui.onboard.contact.dialog.DialogGetContactsLoadingFragment
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.ContactSavingUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactFromSettingsFragment :
    BaseFragment<FragmentAddContactFromSettingsBinding>(R.layout.fragment_add_contact_from_settings) {

    private val viewModel: AddContactFromSettingViewModel by viewModels()
    private val contactAdapter by lazy { ContactAdapter(this::setCheckbox) }
    private val loadingDialog by lazy { DialogGetContactsLoadingFragment() }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            initGrantedView()
        } else {
            binding.tvContactPermission.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initDefaultButtons()
    }

    private fun initDefaultButtons() {
        binding.tvContactPermission.setOnClickListener {
            val needPermissionDialog = NeedPermissionDialog()
            needPermissionDialog.show(
                childFragmentManager,
                NeedPermissionDialog.TAG
            )
        }
        binding.ivBackIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initGrantedView() {
        initButtons()
        initAdapter()
        observeLoadingUiState()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestContactPermission()
        } else {
            initGrantedView()
        }
    }

    private fun initButtons() = with(binding) {
        btnLoad.setOnClickListener {
            viewModel.saveFriends(contactAdapter.addSet.toList())
        }
        cbSelectAll.setOnClickListener {
            if (cbSelectAll.isChecked) {
                contactAdapter.selectAllItem()
            } else {
                contactAdapter.removeAllItem()
            }
        }
    }

    private fun initAdapter() {
        binding.rvContactList.adapter = contactAdapter
    }

    private fun observeLoadingUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingUIState.collect { newState ->
                    when (newState) {
                        ContactSavingUiState.Loading -> {
                            binding.pbLoading.visibility = View.VISIBLE
                            binding.tvWait.visibility = View.VISIBLE
                        }
                        ContactSavingUiState.LoadingDone -> {
                            binding.pbLoading.visibility = View.GONE
                            binding.tvWait.visibility = View.GONE
                            contactAdapter.submitList(viewModel.contactList)
                        }
                        ContactSavingUiState.Dialog -> {
                            loadingDialog.show(
                                childFragmentManager,
                                DialogGetContactsLoadingFragment.TAG
                            )
                        }
                        ContactSavingUiState.DialogDone -> {
                            if (loadingDialog.isVisible) loadingDialog.dismiss()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun setCheckbox(state: Boolean) {
        binding.cbSelectAll.isChecked = state
    }

    private fun requestContactPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }
}