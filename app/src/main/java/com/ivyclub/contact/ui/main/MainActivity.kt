package com.ivyclub.contact.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivityMainBinding
import com.ivyclub.contact.service.PlanReminderNotification.NOTIFICATION
import com.ivyclub.contact.service.PlanReminderNotification.NOTI_PLAN_ID
import com.ivyclub.contact.ui.main.friend.FriendFragment
import com.ivyclub.contact.ui.main.friend.FriendFragmentDirections
import com.ivyclub.contact.ui.onboard.OnBoardingActivity
import com.ivyclub.contact.ui.password.PasswordActivity
import com.ivyclub.contact.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var getOnBoardingResult: ActivityResultLauncher<Intent>
    private lateinit var getOnPasswordResult: ActivityResultLauncher<Intent>

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        setNavigation()
        viewModel.checkOnBoarding()
        setOnBoardingResult()
        viewModel.checkPasswordOnCreate()
        setOnPasswordResult()
    }

    override fun onPause() {
        super.onPause()
        viewModel.lock()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkPasswordOnResume()
        val fromNotification = intent.getBooleanExtra(NOTIFICATION, false)
        val planId = intent.getLongExtra(NOTI_PLAN_ID, -1L)
        if (fromNotification) {
            when (planId) {
                -1L -> binding.bnvMain.selectedItemId = R.id.navigation_plan
                else -> {
                    navController.navigate(
                        FriendFragmentDirections.actionNavigationFriendToPlanDetailsFragment(
                            planId
                        )
                    )
                }
            }
        }
    }

    private fun setOnBoardingResult() {
        getOnBoardingResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.setShowOnBoardingState(false)
                val friendFragment =
                    supportFragmentManager.fragments[0].childFragmentManager.fragments[0]
                if (friendFragment is FriendFragment) {
                    friendFragment.loadFriendList()
                }
            }
        }
    }

    private fun setObserver() {
        viewModel.onBoard.observe(this, {
            if (it) {
                val intent = Intent(this, OnBoardingActivity::class.java)
                getOnBoardingResult.launch(intent)
            }
        })
        viewModel.moveToConfirmPassword.observe(this) {
            val intent = Intent(this, PasswordActivity::class.java)
            getOnPasswordResult.launch(intent)
        }
    }

    private fun setOnPasswordResult() {
        getOnPasswordResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    viewModel.unlock()
                }
            }
    }

    private fun setNavigation() {
        val navView: BottomNavigationView = binding.bnvMain
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }
}