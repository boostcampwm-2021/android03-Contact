package com.ivyclub.contact.ui.main.settings.group

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentManageGroupBinding
import com.ivyclub.contact.ui.main.friend.dialog.GroupDialogFragment
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageGroupFragment :
    BaseFragment<FragmentManageGroupBinding>(R.layout.fragment_manage_group), DialogInterface.OnDismissListener {

    private val viewModel: ManageGroupViewModel by viewModels()
    private val groupListAdapter: GroupListAdapter by lazy {
        GroupListAdapter(
            viewModel::showEditDialog,
            viewModel::showDeleteDialog
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvGroupList.adapter = groupListAdapter
        observeGroupList()
        initBackButton()
        observeShowDialog()
    }

    private fun initBackButton() {
        binding.ivBackIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeGroupList() {
        viewModel.groupList.observe(viewLifecycleOwner) {
            groupListAdapter.submitList(it)
        }
    }

    private fun observeShowDialog() {
        viewModel.showDeleteDialog.observe(viewLifecycleOwner) { group ->
            if (context != null) {
                AlertDialog.Builder(requireContext())
                    .setMessage(String.format(getString(R.string.manage_group_delete), group.name))
                    .setPositiveButton(R.string.yes) { _, _ ->
                        viewModel.deleteGroup(group)
                    }
                    .setNegativeButton(R.string.no) { _, _ ->

                    }
                    .show()
            }
        }

        viewModel.showEditDialog.observe(viewLifecycleOwner) { group ->
            val editDialog = GroupDialogFragment(group)
            editDialog.show(childFragmentManager, EDIT_GROUP_NAME_DIALOG_TAG)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        viewModel.loadGroupList()
        // Snackbar.make(binding.root, getString(R.string.manage_group_success_edit_name), Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val EDIT_GROUP_NAME_DIALOG_TAG = "EditGroupNameDialog"
    }
}
