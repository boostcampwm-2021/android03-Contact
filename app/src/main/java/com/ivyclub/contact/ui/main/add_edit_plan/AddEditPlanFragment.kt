package com.ivyclub.contact.ui.main.add_edit_plan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddEditPlanBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.addChips
import dagger.hilt.android.AndroidEntryPoint
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
                // TODO: 일정 저장
            }
            ivBtnDeletePlan.setOnClickListener {
                // TODO: 일정 삭제
            }
        }
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
            binding.flPlanParticipants.addChips(it.map { pair -> pair.second }) { index ->
                viewModel.removeParticipant(index)
            }
        }
    }

    private fun setAutoCompleteAdapter(friendList: List<Pair<String, String>>) {
        if (context == null) return
        val autoCompleteAdapter = FriendAutoCompleteAdapter(requireContext(), friendList)
        with(binding.actPlanParticipants) {
            setOnItemClickListener { _, _, i, l ->
                (adapter as FriendAutoCompleteAdapter).getItem(i)?.let {
                    viewModel.addParticipant(it)
                    text = null
                }
            }

            setAdapter(autoCompleteAdapter)
        }
    }
}