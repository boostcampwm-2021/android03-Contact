package com.ivyclub.contact.ui.main.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
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
        binding.tvGetContacts.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_settingsContactFragment)
        }
        binding.tvNotificationTime.setOnClickListener {
            val notificationTimeDialog = NotificationTimeDialogFragment()
            notificationTimeDialog.show(childFragmentManager, NotificationTimeDialogFragment.TAG)
        }
        binding.tvSecurity.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_securityFragment)
        }
        binding.tvManageGroup.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_manageGroupFragment)
        }
        binding.tvOssLicense.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_licenseFragment)
        }
    }
}