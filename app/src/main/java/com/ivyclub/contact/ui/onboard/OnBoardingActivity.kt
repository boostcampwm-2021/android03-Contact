package com.ivyclub.contact.ui.onboard

import android.os.Bundle
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivityOnBoardingBinding
import com.ivyclub.contact.util.BaseActivity

class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
    }
}