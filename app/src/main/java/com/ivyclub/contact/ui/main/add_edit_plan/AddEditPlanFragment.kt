package com.ivyclub.contact.ui.main.add_edit_plan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddEditPlanBinding
import com.ivyclub.contact.util.*
import com.ivyclub.data.model.SimpleFriendData
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import java.text.SimpleDateFormat

@AndroidEntryPoint
class AddEditPlanFragment :
    BaseFragment<FragmentAddEditPlanBinding>(R.layout.fragment_add_edit_plan) {

    private val viewModel: AddEditPlanViewModel by viewModels()
    private val args: AddEditPlanFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.dateFormat = SimpleDateFormat(getString(R.string.format_simple_date))

        checkFrom()
        setButtonClickListeners()
        setObservers()
    }

    private fun setButtonClickListeners() {
        with(binding) {
            ivBtnEditPlanFinish.setOnClickListener {
                this@AddEditPlanFragment.viewModel.savePlan(args.planId)
            }
            ivBtnDeletePlan.setOnClickListener {
                // TODO: 일정 삭제
            }
            tvPlanTime.setOnClickListener {
                this@AddEditPlanFragment.viewModel.planTime.value?.let {
                    showDatePickerDialog(it)
                }
            }
        }
    }

    private fun showDatePickerDialog(date: Date) {
        if (context == null) return

        DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                showTimePickerDialog(date.getNewDate(y, m, d))
            }, date.getExactYear(), date.getExactMonth() - 1, date.getDayOfMonth()
        ).show()
    }

    private fun showTimePickerDialog(date: Date) {
        if (context == null) return

        TimePickerDialog(
            requireContext(),
            { _, h, m -> viewModel.setNewDate(date.getNewTime(h, m)) },
            date.getHour(), date.getMinute(), false
        ).show()
    }

    private fun checkFrom() {
        if (args.planId != -1L) viewModel.getLastPlan(args.planId)
    }

    private fun setObservers() {
        viewModel.friendList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty() && binding.actPlanParticipants.adapter == null) {
                setAutoCompleteAdapter(it)
            }
        }

        viewModel.planParticipants.observe(viewLifecycleOwner) {
            binding.flPlanParticipants.addChips(it.map { pair -> pair.name }) { index ->
                viewModel.removeParticipant(index)
            }
        }
    }

    private fun setAutoCompleteAdapter(friendList: List<SimpleFriendData>) {
        if (context == null) return
        val autoCompleteAdapter = FriendAutoCompleteAdapter(requireContext(), friendList)
        with(binding.actPlanParticipants) {
            setOnItemClickListener { _, _, i, _ ->
                (adapter as FriendAutoCompleteAdapter).getItem(i)?.let {
                    viewModel.addParticipant(it)
                    text = null
                }
            }

            setAdapter(autoCompleteAdapter)
        }
    }
}