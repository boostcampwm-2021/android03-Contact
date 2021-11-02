package com.ivyclub.contact.ui.onboard.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentNotificationTimeBinding
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.EntryPoint
import kotlin.math.roundToInt


class NotificationTimeFragment : BaseFragment<FragmentNotificationTimeBinding>(R.layout.fragment_notification_time) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRangeSlider()
        initButtons()
    }

    private fun initButtons() {
        binding.btnNext.setOnClickListener {

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