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
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentPasswordBinding
import com.ivyclub.contact.service.password_timer.PasswordTimerWorker
import com.ivyclub.contact.ui.main.MainActivity
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.PasswordViewType
import dagger.hilt.android.AndroidEntryPoint

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
        initBackPressedListener()
        observeMoveFragment()
        observeFocusedEditTextIndex()
        observeShowSnackBar()
        blockKeyboard()
    }

    override fun onStart() {
        super.onStart()
        if (args.passwordViewType == PasswordViewType.APP_CONFIRM_PASSWORD || args.passwordViewType == PasswordViewType.SECURITY_CONFIRM_PASSWORD) {
            viewModel.initTryCountState()
            observePasswordTimer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (args.passwordViewType == PasswordViewType.APP_CONFIRM_PASSWORD || args.passwordViewType == PasswordViewType.SECURITY_CONFIRM_PASSWORD) {
            viewModel.stopObservePasswordTimer()
        }
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
                observeTimer()
                observeTryCount()
                observeNumberButtonClickable()
            }
            PasswordViewType.SECURITY_CONFIRM_PASSWORD -> {
                viewModel.initPasswordViewType(args.passwordViewType, args.password)
                viewModel.retry.observe(viewLifecycleOwner) {
                    binding.tvPassword.text = getString(R.string.password_retry_message)
                    vibrate()
                }
                observeTimer()
                observeTryCount()
                observeNumberButtonClickable()
            }
        }
    }

    private fun observePasswordTimer() {
        viewModel.observePasswordTimer(activationPasswordButton, updateTimer)
    }

    private fun observeTryCount() {
        viewModel.tryCount.observe(viewLifecycleOwner) { tryCount ->
            if (tryCount == 10) {
                binding.tvPassword.text = getString(R.string.password_wrong_ten_times)
                numberButtonList.forEach {
                    it.isClickable = false
                }
                updateTimer()
                viewModel.timer.observe(viewLifecycleOwner) {
                    binding.tvTryAfter.isVisible = true
                    binding.tvTryAfter.text = String.format(getString(R.string.format_password_try_after), it/60 + 1)
                }
                observePasswordTimer()
            } else {
                activationPasswordButton()
            }
        }
    }

    private fun observeNumberButtonClickable() {
        viewModel.isNumberButtonClickable.observe(viewLifecycleOwner) { isClickable ->
            if (!isClickable) {
                numberButtonList.forEach {
                    it.isClickable = false
                }
            }
        }
    }

    private val updateTimer = {
        viewModel.getTimerInfo()
    }

    private val activationPasswordButton = {
        binding.tvPassword.text = getString(R.string.password_input_password)
        binding.tvTryAfter.isVisible = false
        numberButtonList.forEach {
            it.isClickable = true
        }
    }

    private fun observeTimer() {
        val workName = "PasswordTimer"

        viewModel.setTimer.observe(viewLifecycleOwner) {
            val workRequest = OneTimeWorkRequestBuilder<PasswordTimerWorker>().build()
            context?.let { context ->
                WorkManager.getInstance(context)
                    .enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, workRequest)
            }
        }

        viewModel.stopTimer.observe(viewLifecycleOwner) {
            context?.let { context -> WorkManager.getInstance(context).cancelUniqueWork(workName) }
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

    private fun observeMoveFragment() {
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