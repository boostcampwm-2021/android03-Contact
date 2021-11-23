package com.ivyclub.contact.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivitySplashBinding
import com.ivyclub.contact.ui.main.MainActivity
import com.ivyclub.contact.util.BaseActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },1500)
    }
}