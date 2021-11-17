package com.ivyclub.contact.ui.main.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentSettingsBinding
import com.ivyclub.contact.ui.main.settings.dialog.NotificationTimeDialogFragment
import com.ivyclub.contact.util.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackIcon()
        initSettingButton()
        initOtherButton()
        initAlarmButton()
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
        binding.tvSetNotification.setOnClickListener {
            binding.switchSetAlarm.isChecked = !binding.switchSetAlarm.isChecked
        }
    }

    private fun initAlarmButton() {
        binding.switchSetAlarm.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.tvNotificationTime.visibility = View.VISIBLE
            } else {
                binding.tvNotificationTime.visibility = View.GONE
            }
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