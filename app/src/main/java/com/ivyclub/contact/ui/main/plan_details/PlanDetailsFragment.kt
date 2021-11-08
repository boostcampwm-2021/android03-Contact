package com.ivyclub.contact.ui.main.plan_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentPlanDetailsBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.setFriendChips
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class PlanDetailsFragment : BaseFragment<FragmentPlanDetailsBinding>(R.layout.fragment_plan_details) {

    private val viewModel: PlanDetailsViewModel by viewModels()
    private val args: PlanDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.dateFormat = SimpleDateFormat(getString(R.string.format_simple_date))

        fetchPlanDetails()
        setObservers()
    }

    private fun setObservers() {
        viewModel.planParticipants.observe(viewLifecycleOwner) {
            binding.cgPlanParticipants.setFriendChips(it)
        }
    }

    private fun fetchPlanDetails() {
        viewModel.getPlanDetails(args.planId)
    }
}