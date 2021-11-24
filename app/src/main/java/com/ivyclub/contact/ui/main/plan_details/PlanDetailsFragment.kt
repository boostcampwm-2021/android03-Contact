package com.ivyclub.contact.ui.main.plan_details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentPlanDetailsBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.setFriendChips
import com.ivyclub.contact.util.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class PlanDetailsFragment :
    BaseFragment<FragmentPlanDetailsBinding>(R.layout.fragment_plan_details) {

    private val viewModel: PlanDetailsViewModel by viewModels()
    private val args: PlanDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.dateFormat = SimpleDateFormat(getString(R.string.format_simple_date))

        fetchPlanDetails()
        setObservers()
        setEditPlanButton()
    }

    private fun setEditPlanButton() {
        with(binding) {
            ivBtnBack.setOnClickListener { findNavController().popBackStack() }
            ivBtnEditPlan.setOnClickListener {
                findNavController().navigate(
                    PlanDetailsFragmentDirections.actionPlanDetailsFragmentToAddEditFragment(
                        args.planId
                    )
                )
            }
            ivBtnDeletePlan.setOnClickListener {
                showDeletePlanDialog()
            }
        }
    }

    private fun showDeletePlanDialog() {
        context?.showAlertDialog(getString(R.string.ask_delete_plan), {
            viewModel.deletePlan()
        })
    }

    private fun setObservers() {
        viewModel.planParticipants.observe(viewLifecycleOwner) {
            binding.cgPlanParticipants.setFriendChips(it)
        }

        viewModel.snackbarMessage.observe(viewLifecycleOwner) {
            if (context == null) return@observe
            Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
        }

        viewModel.finishEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun fetchPlanDetails() {
        viewModel.getPlanDetails(args.planId)
    }
}