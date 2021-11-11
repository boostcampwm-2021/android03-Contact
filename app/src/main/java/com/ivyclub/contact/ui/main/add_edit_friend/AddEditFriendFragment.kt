package com.ivyclub.contact.ui.main.add_edit_friend

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddEditFriendBinding
import com.ivyclub.contact.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date

@AndroidEntryPoint
class AddEditFriendFragment :
    BaseFragment<FragmentAddEditFriendBinding>(R.layout.fragment_add_edit_friend) {

    private val viewModel: AddEditFriendViewModel by viewModels()
    val extraInfoListAdapter by lazy { ExtraInfoListAdapter(viewModel::removeExtraInfo) }
    private val args: AddEditFriendFragmentArgs by navArgs()
    lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.fragment = this
        observeGroups()
        observeExtraInfos()
        observeRequiredState()
        initClickListener()
        initBackPressedListener()
    }

    private fun setFriendData() {
        viewModel.getFriendData(args.friendId)
        viewModel.friendData.observe(viewLifecycleOwner) { friendData ->
            binding.apply {
                etName.setText(friendData.name)
                etPhoneNumber.setText(friendData.phoneNumber)
                tvBirthdayValue.text = friendData.birthday
                spnGroup.setSelection(spinnerAdapter.getPosition(friendData.groupName))
            }
            viewModel.addExtraInfoList(friendData.extraInfo)
        }
    }

    private fun initClickListener() {
        with(binding) {
            ivBackIcon.setOnClickListener {
                showBackPressedDialog()
            }

            ivSaveIcon.setOnClickListener {
                this@AddEditFriendFragment.viewModel.checkRequiredNotEmpty(
                    etPhoneNumber.text.toString(),
                    etName.text.toString()
                )
            }

            tvBirthdayValue.setOnClickListener {
                val today = Date(System.currentTimeMillis())
                val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    tvBirthdayValue.text = "${year}.${month + 1}.${day}"
                    this@AddEditFriendFragment.viewModel.showClearButtonVisible(true)
                }
                DatePickerDialog(
                    requireContext(),
                    listener,
                    today.getExactYear(),
                    today.getExactMonth() - 1,
                    today.getDayOfMonth()
                ).show()
            }

            ivClearBirthday.setOnClickListener {
                tvBirthdayValue.text = ""
                this@AddEditFriendFragment.viewModel.showClearButtonVisible(false)
            }
        }
    }

    private fun observeRequiredState() {
        viewModel.canSaveFriendData.observe(viewLifecycleOwner) {
            if (it) {
                with(binding) {
                    this@AddEditFriendFragment.viewModel.saveFriendData(
                        etPhoneNumber.text.toString(),
                        etName.text.toString(),
                        tvBirthdayValue.text.toString(),
                        spnGroup.selectedItem.toString(),
                        extraInfoListAdapter.currentList,
                        args.friendId
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun observeGroups() {
        viewModel.groups.observe(viewLifecycleOwner) {
            initSpinnerAdapter(it)
            setFriendData()
        }
    }

    private fun observeExtraInfos() {
        viewModel.extraInfos.observe(viewLifecycleOwner) {
            extraInfoListAdapter.submitList(it.toMutableList())
        }
    }

    private fun showBackPressedDialog() {
        requireContext().showAlertDialog(getString(R.string.ask_back_while_edit), {
            findNavController().popBackStack()
        })
    }

    private fun initBackPressedListener() {
        if (activity == null) return
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showBackPressedDialog()
        }
    }

    private fun initSpinnerAdapter(groups: List<String>) {
        if (context == null) return
        spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnGroup.adapter = spinnerAdapter
    }
}