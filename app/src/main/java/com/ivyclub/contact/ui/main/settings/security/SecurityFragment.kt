package com.ivyclub.contact.ui.main.settings.security

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentSecurityBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.PasswordViewType

class SecurityFragment : BaseFragment<FragmentSecurityBinding>(R.layout.fragment_security) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPassword.setOnClickListener {
            findNavController().navigate(
                SecurityFragmentDirections.actionSecurityFragmentToPasswordFragment(
                    PasswordViewType.SET_PASSWORD
                )
            )
        }
    }
}