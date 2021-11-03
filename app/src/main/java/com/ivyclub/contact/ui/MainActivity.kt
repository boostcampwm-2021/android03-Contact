package com.ivyclub.contact.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivityMainBinding
import com.ivyclub.contact.ui.onboard.OnBoardingActivity
import com.ivyclub.contact.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigation()
        setObserver()
    }

    private fun setObserver() {
        viewModel.onBoard.observe(this,{
            val intent = Intent(this,OnBoardingActivity::class.java)
            startActivity(intent)
        })
    }

    private fun setNavigation() {
        val navView: BottomNavigationView = binding.bnvMain
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }
}