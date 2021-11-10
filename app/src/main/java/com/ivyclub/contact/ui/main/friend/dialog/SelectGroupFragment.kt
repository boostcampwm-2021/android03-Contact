package com.ivyclub.contact.ui.main.friend.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentSelectGroupDialogBinding

class SelectGroupFragment : DialogFragment() {

    private lateinit var binding: FragmentSelectGroupDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true // 뒤로가기 클릭시 취소
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        initMoveButton()
        initCancelButton()
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("hi", "2", "친구", "동료")
        ) // todo context 수정
        binding.spnGroup.adapter = spinnerAdapter
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

    companion object {
        const val TAG = "SELECT_GROUP_FRAGMENT"
    }
}