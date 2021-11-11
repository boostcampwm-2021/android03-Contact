package com.ivyclub.contact.ui.main.add_friend

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddFriendBinding
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendFragment : BaseFragment<FragmentAddFriendBinding>(R.layout.fragment_add_friend) {

    private val viewModel: AddFriendViewModel by viewModels()
    val extraInfoListAdapter = ExtraInfoListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.fragment = this
        observeGroups()
        observeExtraInfos()
        observeRequiredState()
        initClickListener()
    }

    private fun initClickListener() {
        with(binding) {
            ivBackIcon.setOnClickListener {
                findNavController().popBackStack()
            }

            ivSaveIcon.setOnClickListener {
                this@AddFriendFragment.viewModel.checkRequiredNotEmpty(
                    etPhoneNumber.text.toString(),
                    etName.text.toString()
                )
            }
        }
    }

    private fun observeRequiredState() {
        viewModel.canSaveNewFriend.observe(viewLifecycleOwner) {
            if (it) {
                with(binding) {
                    this@AddFriendFragment.viewModel.saveNewFriend(
                        etPhoneNumber.text.toString(),
                        etName.text.toString(),
                        etBirthday.text.toString(),
                        spnGroup.selectedItem.toString(),
                        extraInfoListAdapter.currentList
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun observeGroups() {
        viewModel.groups.observe(viewLifecycleOwner) {
            initSpinnerAdapter(it)
        }
    }

    private fun observeExtraInfos() {
        viewModel.extraInfos.observe(viewLifecycleOwner) {
            extraInfoListAdapter.submitList(it.toMutableList())
        }
    }

    private fun initSpinnerAdapter(groups: List<String>) {
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnGroup.adapter = spinnerAdapter
    }
}