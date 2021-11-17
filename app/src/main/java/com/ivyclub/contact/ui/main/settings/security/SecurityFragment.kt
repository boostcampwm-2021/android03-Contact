package com.ivyclub.contact.ui.main.settings.security

import android.os.Bundle
import android.view.View
import androidx.biometric.BiometricManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentSecurityBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.PasswordViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecurityFragment : BaseFragment<FragmentSecurityBinding>(R.layout.fragment_security) {

    private val viewModel: SecurityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.initSecurityState()
        observeMoveFragment()
        initFingerPrintButtonClickListener()
    }

    private fun initFingerPrintButtonClickListener() {
        binding.btnFingerPrint.setOnClickListener {
            when (BiometricManager.from(requireContext()).canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    viewModel.setFingerPrint()
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Snackbar.make(binding.root, getString(R.string.biometric_error_hw_unavailable), Snackbar.LENGTH_SHORT).show()
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Snackbar.make(binding.root, getString(R.string.biometric_error_none_enrolled), Snackbar.LENGTH_SHORT).show()
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Snackbar.make(binding.root, getString(R.string.biometric_error_hw_unavailable), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeMoveFragment() {
        viewModel.moveToConfirmPassword.observe(viewLifecycleOwner) { password ->
            if (password.isNotEmpty()) {
                findNavController().navigate(
                    SecurityFragmentDirections.actionSecurityFragmentToPasswordFragment(
                        PasswordViewType.SECURITY_CONFIRM_PASSWORD,
                        password
                    )
                )
            }
        }
        viewModel.moveToSetPassword.observe(viewLifecycleOwner) {
            findNavController().navigate(
                SecurityFragmentDirections.actionSecurityFragmentToPasswordFragment(
                    PasswordViewType.SET_PASSWORD
                )
            )
        }
    }
}