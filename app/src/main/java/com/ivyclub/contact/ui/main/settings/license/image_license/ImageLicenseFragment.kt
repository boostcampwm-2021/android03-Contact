package com.ivyclub.contact.ui.main.settings.license.image_license

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentImageLicenseBinding
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageLicenseFragment :
    BaseFragment<FragmentImageLicenseBinding>(R.layout.fragment_image_license) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackIcon()
    }

    private fun initBackIcon() {
        binding.ivBackIcon.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }
    }

}