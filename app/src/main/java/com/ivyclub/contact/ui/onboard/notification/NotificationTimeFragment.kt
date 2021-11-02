package com.ivyclub.contact.ui.onboard.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentNotificationTimeBinding
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class NotificationTimeFragment : BaseFragment<FragmentNotificationTimeBinding>(R.layout.fragment_notification_time) {

    private val viewModel: NotificationTimeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRangeSlider()
        initButtons()
    }

    private fun initButtons() {
        binding.btnNext.setOnClickListener {
            viewModel.setTime(binding.rsTimeRange.values)
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
}