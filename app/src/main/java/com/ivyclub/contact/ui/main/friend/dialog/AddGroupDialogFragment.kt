package com.ivyclub.contact.ui.main.friend.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.DialogFriendBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupDialogFragment : DialogFragment() {

    private var _binding: DialogFriendBinding? = null
    private val binding get() = _binding ?: error("binding이 초기화되지 않았습니다.")
    private val viewModel: AddGroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_friend, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        with(binding) {
            btnCancel.setOnClickListener {
                dismiss()
            }

            btnAddNewGroup.setOnClickListener {
                val groupName = etNewGroupName.text.toString()
                this@AddGroupDialogFragment.viewModel.saveGroupData(groupName)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}