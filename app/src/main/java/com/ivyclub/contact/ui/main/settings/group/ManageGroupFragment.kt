package com.ivyclub.contact.ui.main.settings.group

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentManageGroupBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageGroupFragment : BaseFragment<FragmentManageGroupBinding>(R.layout.fragment_manage_group) {

    private val viewModel: ManageGroupViewModel by viewModels()
    private val groupListAdapter: GroupListAdapter by lazy { GroupListAdapter(viewModel::showDeleteDialog) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvGroupList.adapter = groupListAdapter
        observeGroupList()
        observeShowDeleteDialog()
    }

    private fun observeGroupList() {
        viewModel.groupList.observe(viewLifecycleOwner) {
            groupListAdapter.submitList(it)
        }
    }

    private fun observeShowDeleteDialog() {
        viewModel.showDeleteDialog.observe(viewLifecycleOwner) { group ->
            AlertDialog.Builder(requireContext())
                .setMessage("그룹 '${group.name}'을(를) 삭제하시겠습니까?")
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deleteGroup(group)
                }
                .setNegativeButton(R.string.no) { _, _ ->

                }
                .show()
        }
    }
}
