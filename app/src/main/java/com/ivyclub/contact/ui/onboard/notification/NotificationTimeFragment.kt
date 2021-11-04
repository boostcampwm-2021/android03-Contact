package com.ivyclub.contact.ui.onboard.notification

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentNotificationTimeBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.SkipDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class NotificationTimeFragment : BaseFragment<FragmentNotificationTimeBinding>(R.layout.fragment_notification_time) {

    private val viewModel: NotificationTimeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRangeSlider()
        initButtons()
        initAppBar()
    }

    private fun initButtons() {
        binding.btnNext.setOnClickListener {
            viewModel.setNotificationOnOff(binding.swOnOff.isChecked.toString())//binding.swOnOff.isChecked로 판단하기
            viewModel.setTime(binding.rsTimeRange.values)
            findNavController().navigate(R.id.action_notificationTimeFragment_to_addContactFragment)
        }
    }

    private fun initRangeSlider() {
        with(binding.rsTimeRange) {
            values = listOf(8f,22f)
            setLabelFormatter{ value:Float ->
                return@setLabelFormatter "${value.roundToInt()}시"
            }
        }
    }

    private fun initAppBar() {
        binding.tbNotificationTime.inflateMenu(R.menu.menu_on_boarding)
        binding.tbNotificationTime.setOnMenuItemClickListener {
            if(it.itemId == R.id.skip) {
                SkipDialog(ok,context).showDialog()
            }
            true
        }
    }
    private val ok = DialogInterface.OnClickListener { _, _ ->
        requireActivity().finish()
    }
}