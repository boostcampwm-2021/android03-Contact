package com.ivyclub.contact.ui.main.settings.security

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
        observeMoveFragment()
    }

    private fun observeMoveFragment() {
        viewModel.moveToSetPassword.observe(viewLifecycleOwner) {
            findNavController().navigate(
                SecurityFragmentDirections.actionSecurityFragmentToPasswordFragment(
                    PasswordViewType.SET_PASSWORD
                )
            )
        }
    }
}