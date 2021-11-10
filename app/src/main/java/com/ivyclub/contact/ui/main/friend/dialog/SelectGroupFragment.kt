package com.ivyclub.contact.ui.main.friend.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentSelectGroupDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectGroupFragment : DialogFragment() {

    private lateinit var binding: FragmentSelectGroupDialogBinding
    private val viewModel: SelectGroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true // 뒤로가기 클릭시 취소
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select_group_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        initMoveButton()
        initCancelButton()
        observerGroupList()
    }

    private fun initMoveButton() {
        binding.tvMove.setOnClickListener {
            // todo 실제로 그룹 옮기는 것 구현
            dismiss()
        }
    }

    private fun initCancelButton() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun observerGroupList() {
        viewModel.groupNameList.observe(viewLifecycleOwner) { newGroupNameList ->
            setSpinnerAdapter(newGroupNameList)
        }
    }

    private fun setSpinnerAdapter(groupNameList: List<String>) {
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            groupNameList
        )
        binding.spnGroup.adapter = spinnerAdapter
    }

    companion object {
        const val TAG = "SELECT_GROUP_FRAGMENT"
    }
}