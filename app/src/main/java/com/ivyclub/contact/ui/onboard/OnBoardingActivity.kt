package com.ivyclub.contact.ui.onboard

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivityOnBoardingBinding
import com.ivyclub.contact.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_on_boarding) as NavHostFragment
        val navController = navHostFragment.navController
    }
}