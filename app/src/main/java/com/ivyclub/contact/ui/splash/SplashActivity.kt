package com.ivyclub.contact.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ActivitySplashBinding
import com.ivyclub.contact.ui.main.MainActivity
import com.ivyclub.contact.util.BaseActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private var splashHandler: Handler? = null
    private val moveToMainActivity = Runnable {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashHandler = Handler(Looper.getMainLooper()).also {
            it.postDelayed(moveToMainActivity, 2000)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        splashHandler?.removeCallbacks(moveToMainActivity)
    }
}