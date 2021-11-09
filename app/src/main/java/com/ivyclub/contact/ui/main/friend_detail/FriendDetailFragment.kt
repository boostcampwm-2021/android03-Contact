package com.ivyclub.contact.ui.main.friend_detail

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentFriendDetailBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.data.model.FriendData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendDetailFragment :
    BaseFragment<FragmentFriendDetailBinding>(R.layout.fragment_friend_detail) {
    val me = FriendData(
        "01054985135",
        "Kim MinJi",
        "1900.01.01",
        "부캠동기",
        listOf("aa", "bb"),
        false,
        mapOf("나이" to "32", "성격" to "좋음", "MBTI" to "ESTJ", "학벌" to "좋음", "캐미" to "나쁨")
    )
    private val viewModel: FriendDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDetails()
        initButtons()
    }

    private fun initButtons() {
        with(binding) {
            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${me.phoneNumber}"))
                startActivity(intent)
            }
            btnFavorite.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(context, R.anim.star_animation)
                btnFavorite.startAnimation(animation)
                viewModel.setFavorite(me.phoneNumber, btnFavorite.isChecked)
            }
        }
    }

    private fun initDetails() {
        binding.tvName.text = me.name
        binding.tvGroup.text = me.groupName
        binding.tvPhoneNum.text = me.phoneNumber
        binding.btnFavorite.isChecked = me.isFavorite
        for (key in me.extraInfo.keys) {
            binding.llExtraInfo.addView(getTitle(key))
            binding.llExtraInfo.addView(getContent(me.extraInfo[key] ?: ""))
        }

    }

    private fun getTitle(text: String): TextView {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 100, 0, 0)
        val tv = TextView(context)
        tv.text = text
        tv.setTypeface(null, Typeface.BOLD)
        tv.setTextColor(Color.BLACK)
        tv.textSize = 20f
        tv.layoutParams = layoutParams
        return tv
    }

    private fun getContent(text: String): TextView {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 4, 0, 0)
        val tv = TextView(context)
        tv.text = text
        tv.setTextColor(Color.BLACK)
        tv.textSize = 20f
        tv.layoutParams = layoutParams
        return tv
    }

}