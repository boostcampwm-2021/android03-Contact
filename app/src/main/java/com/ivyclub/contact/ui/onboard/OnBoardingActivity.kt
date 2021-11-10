package com.ivyclub.contact.ui.onboard

import android.content.DialogInterface
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivityOnBoardingBinding
import com.ivyclub.contact.util.BaseActivity
import com.ivyclub.contact.util.SkipDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {

    private lateinit var navHostFragment: NavHostFragment
    private val viewModel: OnBoardingViewModel by viewModels()

    private val ok = DialogInterface.OnClickListener { _, _ ->
        finish()
    }

    override fun onBackPressed() {
        navHostFragment = binding.fcvOnBoarding.getFragment() as NavHostFragment
        if (navHostFragment.childFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            SkipDialog(ok, this).showDialog()
        }
    }

    override fun onDestroy() {
        viewModel.setShowOnBoardingState(false)
        super.onDestroy()
    }
}