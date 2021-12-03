package com.ivyclub.contact.ui.main.plan_details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentParticipantInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipantInfoBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentParticipantInfoBinding
    private val viewModel: ParticipantInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_participant_info,
            container,
            false
        )

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ParticipantInfoBottomSheetFragment.viewModel
        }

        arguments?.getLong(KEY_PARTICIPANT_ID)?.let { participantId ->
            viewModel.getParticipantData(participantId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(view)
        initButtons()
        setObservers()
    }

    private fun setBottomSheetBehavior(view: View) {
        BottomSheetBehavior.from(view.parent as View).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        }
    }

    private fun initButtons() {
        with(binding) {
            ivBtnClose.setOnClickListener { dismiss() }
            tvBtnGoDetails.setOnClickListener {
                arguments?.let { args -> setFragmentResult(REQUEST, args) }
                dismiss()
            }
            ivBtnCall.setOnClickListener {
                this@ParticipantInfoBottomSheetFragment.viewModel.getParticipantPhone()
                    ?.let { phoneNumber ->
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber}"))
                        startActivity(intent)
                    }
            }
        }
    }

    private fun setObservers() {
        viewModel.participantImage.observe(viewLifecycleOwner) {
            binding.ivProfileImage.clipToOutline = true
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.photo)
                .into(binding.ivProfileImage)
        }
    }

    companion object {
        const val TAG = "PARTICIPANT_INFO_FRAGMENT"
        const val KEY_PARTICIPANT_ID = "participantId"
        const val REQUEST = "REQUEST_PARTICIPANT_INFO"
    }
}