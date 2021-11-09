package com.ivyclub.contact.ui.main.add_friend

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddFriendBinding
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendFragment : BaseFragment<FragmentAddFriendBinding>(R.layout.fragment_add_friend) {

    private val viewModel: AddFriendViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGroups()
    }

    private fun observeGroups() {
        viewModel.groups.observe(viewLifecycleOwner) {
            initSpinnerAdapter(it)
        }
    }

    private fun initSpinnerAdapter(groups: List<String>) {
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnGroup.adapter = spinnerAdapter
    }
}