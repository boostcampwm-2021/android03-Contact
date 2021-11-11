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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentFriendDetailBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.data.model.FriendData
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat


@AndroidEntryPoint
class FriendDetailFragment :
    BaseFragment<FragmentFriendDetailBinding>(R.layout.fragment_friend_detail) {
    private val viewModel: FriendDetailViewModel by viewModels()
    private val args: FriendDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        loadFriendDetail(args.friendId)
        initButtons(args.friendId)
    }

    private fun loadFriendDetail(id: Long) {
        viewModel.loadFriendData(id)
    }

    private fun setObserver() {
        viewModel.friendData.observe(this, {
            initDetails(it)
        })
        viewModel.plan1.observe(this, {
            with(binding) {
                llPlan1.visibility = View.VISIBLE
                tvPlan1Title.text = it.title
                tvPlan1Time.text = it.date.toString()
            }
        })
        viewModel.plan2.observe(this, {
            with(binding) {
                llPlan2.visibility = View.VISIBLE
                tvPlan2Title.text = it.title
                tvPlan2Time.text = it.date.toString()
            }
        })
    }

    private fun initButtons(id: Long) {
        with(binding) {
            btnFavorite.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(context, R.anim.star_animation)
                btnFavorite.startAnimation(animation)
                viewModel.setFavorite(id, btnFavorite.isChecked)
            }
            ivEdit.setOnClickListener {
                findNavController().navigate(
                    FriendDetailFragmentDirections.actionFriendDetailFragmentToAddEditFriendFragment(
                        args.friendId
                    )
                )
            }
            ivBackIcon.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initDetails(friend: FriendData) {
        with(binding) {
            tvName.text = friend.name
            tvGroup.text = friend.groupName
            tvPhoneNum.text = friend.phoneNumber
            btnFavorite.isChecked = friend.isFavorite
            for (key in friend.extraInfo.keys) {
                llExtraInfo.addView(getTitle(key))
                llExtraInfo.addView(getContent(friend.extraInfo[key] ?: ""))
            }
            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${friend.phoneNumber}"))
                startActivity(intent)
            }
            //bindPlan(friend.planList)
            demoPlan()
        }

    }

    private fun getTitle(text: String): TextView {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(25, 50, 0, 0)
        return TextView(context).apply {
            this.text = text
            textSize = 16f
            this.layoutParams = layoutParams
        }
    }

    private fun getContent(text: String): TextView {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0,25,0,0)
        return TextView(context).apply {
            this.text = text
            setTextColor(Color.BLACK)
            textSize = 20f
            this.layoutParams = layoutParams
            setBackgroundResource(R.drawable.bg_details)
            setPadding(48,24,48,24)
        }
    }

    private fun bindPlan(planIds: List<Long>) {
        viewModel.loadPlans(planIds)
        //binding.llPlan1.visibility = View.GONE
        //binding.llPlan2.visibility = View.GONE
    }

    private fun demoPlan() {
        viewModel.loadPlans(listOf<Long>(1,2,3,4))
    }
}