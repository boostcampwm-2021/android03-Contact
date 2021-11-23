package com.ivyclub.contact.ui.onboard.app_intro

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentIntroBinding
import com.ivyclub.contact.util.BaseFragment

class IntroFragment : BaseFragment<FragmentIntroBinding>(R.layout.fragment_intro) {
    private val introImages =
        listOf(R.drawable.intro0, R.drawable.intro1, R.drawable.intro2, R.drawable.intro3)
    private val introString = listOf(
        "안녕하세요\n친구와 지인의 정보를 기록할 수 있는\n컨택트입니다.",
        "지인과의 약속을 저장해주시면,\n기억하고 있다가 알림으로 알려드릴게요.",
        "걱정하지 마세요.\n컨택을 보호하기 위해 비밀번호와\n지문인식 기능을 이용할 수 있어요.",
        "지금 바로 시작해봐요.",
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("temp", ".${getString(R.string.fragment_intro_introduce_string_4)}")
        initPage()
    }

    private fun initPage() {
        with(binding) {
            vpIntro.adapter =
                ViewPagerAdapter(introImages, introString, this@IntroFragment::navigateToNext)
            vpIntro.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            sdicIndicator.setViewPager2(binding.vpIntro)
            tvSkip.setOnClickListener {
                navigateToNext()
            }
        }
    }

    private fun navigateToNext() {
        findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToNotificationTimeFragment())
    }

}