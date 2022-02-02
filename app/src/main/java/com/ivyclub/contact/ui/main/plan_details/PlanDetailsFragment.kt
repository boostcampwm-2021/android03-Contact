package com.ivyclub.contact.ui.main.plan_details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentPlanDetailsBinding
import com.ivyclub.contact.ui.main.plan_details.ParticipantInfoBottomSheetFragment.Companion.KEY_PARTICIPANT_ID
import com.ivyclub.contact.ui.main.plan_details.ParticipantInfoBottomSheetFragment.Companion.REQUEST
import com.ivyclub.contact.ui.main.plan_details.PlanDetailsViewModel.Companion.KEY_PHONE_NUMBERS
import com.ivyclub.contact.ui.main.plan_details.PlanDetailsViewModel.Companion.KEY_PLAN_CONTENT
import com.ivyclub.contact.ui.main.plan_details.PlanDetailsViewModel.Companion.KEY_PLAN_PLACE
import com.ivyclub.contact.ui.main.plan_details.PlanDetailsViewModel.Companion.KEY_PLAN_TIME
import com.ivyclub.contact.ui.main.plan_details.PlanDetailsViewModel.Companion.KEY_PLAN_TITLE
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.setFriendChips
import com.ivyclub.contact.util.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PlanDetailsFragment :
    BaseFragment<FragmentPlanDetailsBinding>(R.layout.fragment_plan_details) {

    private val viewModel: PlanDetailsViewModel by viewModels()
    private val args: PlanDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.dateFormat =
            SimpleDateFormat(getString(R.string.format_simple_date), Locale.getDefault())

        fetchPlanDetails()
        setObservers()
        setEditPlanButton()
        setParticipantInfoResultLauncher()
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

            ivBtnSharePlanToParticipants.setOnClickListener {
                this@PlanDetailsFragment.viewModel.sendMessagesToPlanParticipants()
            }
        }
    }

    private fun showDeletePlanDialog() {
        context?.showAlertDialog(getString(R.string.ask_delete_plan), {
            viewModel.deletePlan()
        })
    }

    private fun setObservers() {
        with(viewModel) {
            planParticipants.observe(viewLifecycleOwner) { participants ->
                binding.cgPlanParticipants.setFriendChips(participants.map { it.name }) { index ->
                    viewModel.goParticipantsDetails(index)
                }
            }

            goFriendDetailsEvent.observe(viewLifecycleOwner) { friendId ->
                showParticipantInfoDialog(friendId)
            }

            sendMessagesToParticipantsEvent.observe(viewLifecycleOwner) { bundle ->
                sendSharePlanMessages(bundle)
            }

            snackbarMessage.observe(viewLifecycleOwner) {
                if (context == null) return@observe
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
            }

            finishEvent.observe(viewLifecycleOwner) {
                findNavController().popBackStack()
            }

            folderExists.observe(viewLifecycleOwner) {
                if (it) {
                    viewModel.getPhotos(args.planId)
                }
            }

            photoIds.observe(viewLifecycleOwner) {
                with(binding) {
                    vpPhoto.adapter = PhotoAdapter(
                        it,
                        args.planId,
                        this@PlanDetailsFragment::moveToImageDetailFragment
                    )
                    vpPhoto.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    if (it.isNotEmpty()) vpPhoto.currentItem = 0
                    sdicIndicator.setViewPager2(vpPhoto)
                }
            }
        }
    }

    private fun moveToImageDetailFragment() {
        val bundle = Bundle()
        bundle.putLong("friendId", -1L) // todo -1L 변경해야 함.
        findNavController().navigate(
            R.id.action_planDetailsFragment_to_imageDetailFragment
        )
    }

    private fun showParticipantInfoDialog(participantId: Long) {
        val bundle = Bundle().apply {
            putLong(KEY_PARTICIPANT_ID, participantId)
        }
        ParticipantInfoBottomSheetFragment().apply {
            arguments = bundle
            showsDialog = true
            show(
                this@PlanDetailsFragment.childFragmentManager,
                ParticipantInfoBottomSheetFragment.TAG
            )
        }
    }

    private fun setParticipantInfoResultLauncher() {
        childFragmentManager.setFragmentResultListener(REQUEST, viewLifecycleOwner) { _, bundle ->
            findNavController().navigate(
                PlanDetailsFragmentDirections
                    .actionPlanDetailsFragmentToFriendDetailFragment(
                        bundle.getLong(
                            KEY_PARTICIPANT_ID
                        )
                    )
            )
        }
    }

    private fun sendSharePlanMessages(bundle: Bundle) {
        val strTo = bundle.getString(KEY_PHONE_NUMBERS)

        val planTitle = bundle.getString(KEY_PLAN_TITLE) ?: return
        val msgPlanTitle = String.format(getString(R.string.format_share_plan_title), planTitle)

        val planTime = bundle.getLong(KEY_PLAN_TIME, -1L)
        if (planTime == -1L) return
        val strPlanTime =
            SimpleDateFormat(getString(R.string.format_simple_date), Locale.getDefault()).format(
                Date(planTime)
            )
        val msgPlanTime = String.format(getString(R.string.format_share_plan_time), strPlanTime)

        val planPlace = bundle.getString(KEY_PLAN_PLACE)
        val msgPlanPlace =
            if (planPlace == null) ""
            else String.format(getString(R.string.format_share_plan_place), planPlace)

        val planContent = bundle.getString(KEY_PLAN_CONTENT)
        val msgPlanContent =
            if (planContent == null) ""
            else String.format(getString(R.string.format_share_plan_place), planContent)

        val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse(strTo))
            .apply {
                putExtra(
                    "sms_body",
                    String.format(
                        getString(R.string.format_share_plan_to_participants),
                        msgPlanTitle + msgPlanTime + msgPlanPlace + msgPlanContent,
                        getString(R.string.app_name)
                    )
                )
            }
        startActivity(smsIntent)
    }

    private fun fetchPlanDetails() {
        viewModel.getPlanDetails(args.planId)
    }
}