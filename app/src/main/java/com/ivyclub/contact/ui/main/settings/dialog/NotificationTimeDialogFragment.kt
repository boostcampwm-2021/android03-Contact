package com.ivyclub.contact.ui.main.settings.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentDialogNotificationTimeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class NotificationTimeDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogNotificationTimeBinding

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_dialog_notification_time,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        initRangeSlider()
        initCancelButton()
    }

    private fun initRangeSlider() = with(binding.rsTimeRange) {
        values = listOf(8f, 22f)
        setLabelFormatter { value ->
            return@setLabelFormatter "${value.roundToInt()}시"
        }
    }

    private fun initCancelButton() {
        binding.tvCancel.setOnClickListener { dismiss() }
    }

    companion object {
        val TAG = "NOTIFICATION_TIME_DIALOG"
    }
}