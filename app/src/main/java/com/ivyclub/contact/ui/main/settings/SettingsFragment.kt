package com.ivyclub.contact.ui.main.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentSettingsBinding
import com.ivyclub.contact.ui.main.settings.dialog.NotificationTimeDialogFragment
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {
    private val viewModel: SettingsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initBackIcon()
        initSettingButton()
        initOtherButton()
    }

    private fun initViewModel() {
        binding.viewModel = viewModel
    }

    private fun initBackIcon() {
        binding.ivBackIcon.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }
    }

    private fun initSettingButton() {
        binding.tvNotificationTime.setOnClickListener {
            val notificationTimeDialog = NotificationTimeDialogFragment()
            notificationTimeDialog.show(childFragmentManager, NotificationTimeDialogFragment.TAG)
        }
    }

    // todo 추후 개발 후 삭제
    private fun initOtherButton() {
        binding.tvGetContacts.setOnClickListener {
            Toast.makeText(requireContext(), "준비중입니다.", Toast.LENGTH_SHORT).show()
        }
        binding.tvSecurity.setOnClickListener {
            Toast.makeText(requireContext(), "준비중입니다.", Toast.LENGTH_SHORT).show()
        }
    }
}