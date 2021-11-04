package com.ivyclub.contact.ui.onboard

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.view.isEmpty
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivityOnBoardingBinding
import com.ivyclub.contact.util.BaseActivity
import com.ivyclub.contact.util.SkipDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {

    private lateinit var  navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_on_boarding) as NavHostFragment
        val navController = navHostFragment.navController
    }

    override fun onBackPressed() {
        navHostFragment = binding.fcvOnBoarding.getFragment() as NavHostFragment
        if(navHostFragment.childFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            SkipDialog(ok,this).showDialog()
        }
    }

    private val ok = DialogInterface.OnClickListener { _, _ ->
        finish()
    }

}