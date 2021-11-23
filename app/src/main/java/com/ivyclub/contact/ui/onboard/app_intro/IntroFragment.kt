package com.ivyclub.contact.ui.onboard.app_intro

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentIntroBinding
import com.ivyclub.contact.util.BaseFragment


class IntroFragment : BaseFragment<FragmentIntroBinding>(R.layout.fragment_intro) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val a = listOf(R.drawable.intro0, R.drawable.intro1,R.drawable.intro2, R.drawable.intro3)
        val b = listOf("첫화면","두번째화면","세번째화면","마지막화면")
        binding.vpIntro.adapter = ViewPagerAdapter(a,b,this::navigateToNext)
        binding.vpIntro.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.sdicIndicator.setViewPager2(binding.vpIntro)
        binding.tvSkip.setOnClickListener {
            navigateToNext()
        }
    }

    private fun navigateToNext() {
        findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToNotificationTimeFragment())
    }
}