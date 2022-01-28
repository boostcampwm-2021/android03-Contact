package com.ivyclub.contact.ui.main.settings.license

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentLicenseBinding
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LicenseFragment : BaseFragment<FragmentLicenseBinding>(R.layout.fragment_license) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackIcon()
        initSettingButton()
    }

    private fun initBackIcon() {
        binding.ivBackIcon.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }
    }

    private fun initSettingButton() {
        binding.tvImageLicense.setOnClickListener {
            findNavController().navigate(R.id.action_licenseFragment_to_imageLicenseFragment)
        }
        binding.tvOssLicense.setOnClickListener {
            startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
        }
    }
}