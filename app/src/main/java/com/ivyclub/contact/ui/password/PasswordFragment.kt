package com.ivyclub.contact.ui.password

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.os.*
import android.view.View
import androidx.activity.addCallback
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentPasswordBinding
import com.ivyclub.contact.service.password_timer.PasswordTimerWorker
import com.ivyclub.contact.ui.main.MainActivity
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.PasswordViewType
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PasswordFragment :
    BaseFragment<FragmentPasswordBinding>(R.layout.fragment_password) {

    private val viewModel: PasswordViewModel by viewModels()
    private val args: PasswordFragmentArgs by navArgs()
    private val passwordEditTextList by lazy {
        with(binding) {
            listOf(etPassword1, etPassword2, etPassword3, etPassword4)
        }
    }
    private val numberButtonList by lazy {
        with(binding) {
            listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        checkFingerPrintState()
        initPasswordViewType()
        initNumberClickListener()
        initCancelButtonClickListener()
        initMoveFragmentObserver()
        initBackPressedListener()
        observeFocusedEditTextIndex()
        observeShowSnackBar()
        blockKeyboard()
    }

    private fun checkFingerPrintState() {
        if (args.passwordViewType == PasswordViewType.APP_CONFIRM_PASSWORD || args.passwordViewType == PasswordViewType.SECURITY_CONFIRM_PASSWORD) {
            viewModel.checkFingerPrintState()
            viewModel.fingerPrint.observe(viewLifecycleOwner) {
                val prompt = createBiometricPrompt()
                val promptInfo = createBiometricPromptInfo()
                prompt.authenticate(promptInfo)
            }
        }
    }

    private fun initBackPressedListener() {
        if (activity == null) return
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            when (args.passwordViewType) {
                PasswordViewType.SECURITY_CONFIRM_PASSWORD -> findNavController().popBackStack(
                    R.id.settingsFragment,
                    false
                )
                else -> findNavController().popBackStack()
            }
        }
    }

    private fun initPasswordViewType() {
        when (args.passwordViewType) {
            PasswordViewType.SET_PASSWORD -> {
                viewModel.initPasswordViewType(args.passwordViewType)
                viewModel.moveToReconfirmPassword.observe(viewLifecycleOwner) { password ->
                    findNavController().navigate(
                        PasswordFragmentDirections.actionSetPasswordFragmentSelf(
                            PasswordViewType.RECONFIRM_PASSWORD,
                            password
                        )
                    )
                }
            }
            PasswordViewType.RECONFIRM_PASSWORD -> {
                viewModel.initPasswordViewType(args.passwordViewType, args.password)
                binding.tvPassword.text = getString(R.string.password_reconfirm_message)

            }
            PasswordViewType.APP_CONFIRM_PASSWORD -> {
                viewModel.initPasswordViewType(args.passwordViewType)
                viewModel.finishConfirmPassword.observe(viewLifecycleOwner) {
                    val intent = Intent(context, MainActivity::class.java)
                    activity?.setResult(RESULT_OK, intent)
                    activity?.finish()
                }
                viewModel.retry.observe(viewLifecycleOwner) {
                    binding.tvPassword.text = getString(R.string.password_retry_message)
                    vibrate()
                }
                observeTryCount()
            }
            PasswordViewType.SECURITY_CONFIRM_PASSWORD -> {
                viewModel.initPasswordViewType(args.passwordViewType, args.password)
                viewModel.retry.observe(viewLifecycleOwner) {
                    binding.tvPassword.text = getString(R.string.password_retry_message)
                    vibrate()
                }
                observeTryCount()
            }
        }
    }

    private fun observeTryCount() {
        viewModel.tryCount.observe(viewLifecycleOwner) { tryCount ->
            if (tryCount == 10) {
                binding.tvPassword.text = "비밀번호를 10회 잘못 입력하셨습니다."
                numberButtonList.forEach {
                    it.isClickable = false
                }
                viewModel.getTimerInfo()
                viewModel.setTimer.observe(viewLifecycleOwner) {
                    val workRequest: WorkRequest =
                        OneTimeWorkRequestBuilder<PasswordTimerWorker>().build()
                    context?.let { context ->
                        WorkManager.getInstance(context)
                            .enqueue(workRequest)
                    }
                }
                viewModel.timer.observe(viewLifecycleOwner) {
                    binding.tvTryCount.text = "${it/60 + 1}분 후에 다시 시도해주세요."
                }
            }
            binding.tvTryCount.text = "시도 횟수 ($tryCount/10)"
        }
    }

    private fun vibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator;
        } else {
            activity?.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }

    private fun initNumberClickListener() {
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
                PasswordFragmentDirections.actionSetPasswordFragmentSelf(
                    PasswordViewType.SET_PASSWORD
                )
            )
        }
        viewModel.moveToPreviousFragment.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun observeShowSnackBar() {
        viewModel.showSnackBar.observe(viewLifecycleOwner) { id ->
            Snackbar.make(binding.root, getString(id), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun blockKeyboard() {
        passwordEditTextList.forEach {
            it.setTextIsSelectable(true)
            it.showSoftInputOnFocus = false
            it.isFocusableInTouchMode = false
        }
    }

    private fun createBiometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_prompt_title))
            .setDescription(getString(R.string.biometric_prompt_description))
            .setNegativeButtonText(getString(R.string.biometric_prompt_cancel))
            .build()
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val authenticationCallback = getAuthenticationCallback()
        return BiometricPrompt(this, executor, authenticationCallback)
    }

    private fun getAuthenticationCallback() = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            viewModel.succeedFingerPrintAuth()
        }
    }
}