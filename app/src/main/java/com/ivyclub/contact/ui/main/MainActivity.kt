package com.ivyclub.contact.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivityMainBinding
import com.ivyclub.contact.service.ContactRemoteViewsFactory.Companion.WIDGET_PLAN_ID
import com.ivyclub.contact.service.plan_reminder.PlanReminderNotification.NOTIFICATION
import com.ivyclub.contact.service.plan_reminder.PlanReminderNotification.NOTI_PLAN_ID
import com.ivyclub.contact.ui.main.friend.FriendFragmentDirections
import com.ivyclub.contact.ui.onboard.OnBoardingActivity
import com.ivyclub.contact.ui.password.PasswordActivity
import com.ivyclub.contact.util.BaseActivity
import com.ivyclub.contact.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var getOnPasswordResult: ActivityResultLauncher<Intent>

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        setNavigation()
        viewModel.checkOnBoarding()
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
        checkFromNotification()
        checkFromWidget()
    }

    private fun checkFromNotification() {
        val fromNotification = intent.getBooleanExtra(NOTIFICATION, false)
        val planId = intent.getLongExtra(NOTI_PLAN_ID, -1L)
        if (fromNotification) {
            when (planId) {
                -1L -> binding.bnvMain.selectedItemId = R.id.navigation_plan
                else -> {
                    if (navController.currentDestination?.id == R.id.navigation_friend) {
                        navController.navigate(
                            FriendFragmentDirections.actionNavigationFriendToPlanDetailsFragment(
                                planId
                            )
                        )
                        resetIntent(NOTIFICATION, NOTI_PLAN_ID)
                    }
                }
            }
        }
    }

    private fun checkFromWidget() {
        val widgetPlanId = intent.getLongExtra(WIDGET_PLAN_ID, -1L)
        if (widgetPlanId != -1L && navController.currentDestination?.id == R.id.navigation_friend) {
            navController.navigate(
                FriendFragmentDirections.actionNavigationFriendToPlanDetailsFragment(
                    widgetPlanId
                )
            )
            resetIntent(WIDGET_PLAN_ID)
        }
    }

    private fun resetIntent(vararg keys: String) {
        val i = intent
        keys.forEach { key -> i.removeExtra(key) }
        intent = i
    }

    private fun setObserver() {
        viewModel.onBoard.observe(this, {
            if (it) {
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_friend, R.id.navigation_plan -> {
                    binding.bnvMain.isVisible = true
                }
                else -> binding.bnvMain.isVisible = false
            }
        }
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        val view = currentFocus
        motionEvent?.let {
            if (
                view != null &&
                (it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_MOVE) &&
                view is EditText
            ) {
                val intArr = IntArray(2)
                view.getLocationOnScreen(intArr)
                val x = it.rawX + view.left - intArr[0]
                val y = it.rawY + view.top - intArr[1]
                if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                    binding.hideKeyboard()
                    view.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(motionEvent)
    }
}