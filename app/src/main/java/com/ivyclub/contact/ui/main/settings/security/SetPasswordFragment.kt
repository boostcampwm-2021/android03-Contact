package com.ivyclub.contact.ui.main.settings.security

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentSetPasswordBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.PasswordViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetPasswordFragment :
    BaseFragment<FragmentSetPasswordBinding>(R.layout.fragment_set_password) {

    private val viewModel: SetPasswordViewModel by viewModels()
    private val args: SetPasswordFragmentArgs by navArgs()
    private val passwordEditTextList by lazy {
        with(binding) {
            listOf(etPassword1, etPassword2, etPassword3, etPassword4)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initPasswordViewType()
        initNumberClickListener()
        initCancelButtonClickListener()
        initMoveFragmentObserver()
        observeFocusedEditTextIndex()
    }

    private fun initPasswordViewType() {
        viewModel.initPasswordViewType(args.passwordViewType, args.password)
        when (args.passwordViewType) {
            PasswordViewType.SET_PASSWORD -> {
                viewModel.moveToReconfirmPassword.observe(viewLifecycleOwner) { password ->
                    findNavController().navigate(
                        SetPasswordFragmentDirections.actionSetPasswordFragmentSelf(
                            PasswordViewType.RECONFIRM_PASSWORD,
                            password
                        )
                    )
                }
            }
            PasswordViewType.RECONFIRM_PASSWORD -> {
                binding.tvPassword.text = "확인을 위해 한번 더 입력해 주세요."

            }
        }
    }

    private fun initNumberClickListener() {
        val numberButtonList = with(binding) {
            listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
        }

        numberButtonList.forEachIndexed { number, button ->
            button.setOnClickListener {
                viewModel.moveFocusFront(number.toString())
            }
        }
    }

    private fun initCancelButtonClickListener() {
        binding.btnCancel.setOnClickListener {
            viewModel.moveFocusBack()
        }
    }

    private fun observeFocusedEditTextIndex() {
        viewModel.focusedEditTextIndex.observe(viewLifecycleOwner) {
            passwordEditTextList[it - 1].requestFocus()
        }
    }

    private fun initMoveFragmentObserver() {
        viewModel.moveToSetPassword.observe(viewLifecycleOwner) {
            findNavController().navigate(
                SetPasswordFragmentDirections.actionSetPasswordFragmentSelf(
                    PasswordViewType.SET_PASSWORD
                )
            )
            Snackbar.make(binding.root, "비밀번호가 일치하지 않습니다.\n처음부터 다시 시도해주세요.", Snackbar.LENGTH_SHORT)
                .show()
        }
        viewModel.moveToPreviousFragment.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}