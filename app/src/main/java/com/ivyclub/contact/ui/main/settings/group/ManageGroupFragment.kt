package com.ivyclub.contact.ui.main.settings.group

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentManageGroupBinding
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageGroupFragment : BaseFragment<FragmentManageGroupBinding>(R.layout.fragment_manage_group) {

    private val viewModel: ManageGroupViewModel by viewModels()
    private val groupListAdapter: GroupListAdapter by lazy { GroupListAdapter(viewModel::deleteGroup) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvGroupList.adapter = groupListAdapter
        observeGroupList()
    }

    private fun observeGroupList() {
        viewModel.groupList.observe(viewLifecycleOwner) {
            groupListAdapter.submitList(it)
        }
    }
}
