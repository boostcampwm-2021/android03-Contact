package com.ivyclub.contact.ui.main.settings.security

import android.os.Bundle
import android.view.View
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentSetPasswordBinding
import com.ivyclub.contact.util.BaseFragment

class SetPasswordFragment :
    BaseFragment<FragmentSetPasswordBinding>(R.layout.fragment_set_password) {

    private var focusedEditTextIndex = 0
    private val passwordEditTextList by lazy {
        with(binding) {
            listOf(etPassword1, etPassword2, etPassword3, etPassword4)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNumberClickListener()
    }

    private fun initNumberClickListener() {
        val numberButtonList = with(binding) {
            listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
        }

        numberButtonList.forEachIndexed { number, button ->
            button.setOnClickListener {
                updateEditText(number)
            }
        }
    }

    private fun updateEditText(number: Int) {
        passwordEditTextList[focusedEditTextIndex].setText(number.toString())
        if (focusedEditTextIndex == passwordEditTextList.lastIndex) {
            // TODO : 비밀번호 입력 종료
        } else {
            passwordEditTextList[++focusedEditTextIndex].requestFocus()
        }
    }
}