package com.ivyclub.contact.ui.main.friend_detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentFriendDetailBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.showAlertDialog
import com.ivyclub.data.model.FriendData
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class FriendDetailFragment :
    BaseFragment<FragmentFriendDetailBinding>(R.layout.fragment_friend_detail) {
    private val viewModel: FriendDetailViewModel by viewModels()
    private val args: FriendDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.dateFormat = SimpleDateFormat(getString(R.string.format_simple_date))

        setObserver()
        loadFriendDetail(args.friendId)
        initButtons(args.friendId)
    }

    private fun loadFriendDetail(id: Long) {
        viewModel.loadFriendData(id)
        viewModel.loadProfileImage(id)?.let {
            Glide.with(this)
                .load(it)
                .into(binding.ivProfileImage)
        }
        binding.ivProfileImage.clipToOutline = true
    }

    private fun setObserver() {
        viewModel.friendData.observe(this, {
            initDetails(it)
        })
        viewModel.finishEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun initButtons(id: Long) {
        with(binding) {
            btnFavorite.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(context, R.anim.star_animation)
                btnFavorite.startAnimation(animation)
                this@FriendDetailFragment.viewModel.setFavorite(id, btnFavorite.isChecked)
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
            tvSeeAllPlan.setOnClickListener {
                findNavController().navigate(
                    FriendDetailFragmentDirections.actionFriendDetailFragmentToFriendAllPlanFragment(
                        args.friendId
                    )
                )
            }
            ivProfileImage.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    ivProfileImage to "secondTransitionName"
                )
                val bundle = Bundle()
                bundle.putLong("friendId", args.friendId)
                findNavController().navigate(
                    R.id.action_friendDetailFragment_to_imageDetailFragment,
                    bundle, // Bundle of args
                    null, // NavOptions
                    extras
                )
            }
            ivDelete.setOnClickListener {
                showDeleteFriendDialog()
            }
        }
    }

    private fun initDetails(friend: FriendData) {
        with(binding) {
            tvName.text = friend.name
            tvGroup.text = friend.groupName
            tvPhoneNum.text = friend.phoneNumber
            if (friend.phoneNumber == "") btnCall.visibility = View.GONE
            btnFavorite.isChecked = friend.isFavorite
            llExtraInfo.removeAllViews()
            if (friend.birthday == "") {
                llBirthday.visibility = View.GONE
            } else {
                llBirthday.visibility = View.VISIBLE
                tvBirthday.text = friend.birthday
            }
            for (key in friend.extraInfo.keys) {
                llExtraInfo.addView(getTitle(key))
                llExtraInfo.addView(getContent(friend.extraInfo[key] ?: ""))
            }
            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${friend.phoneNumber}"))
                startActivity(intent)
            }
            bindPlan(friend.planList)
        }

    }

    private fun getTitle(text: String): TextView {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(24, 48, 0, 0)
        return TextView(context).apply {
            this.text = text
            textSize = 12f
            this.layoutParams = layoutParams
        }
    }

    private fun getContent(text: String): TextView {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 24, 0, 0)
        return TextView(context).apply {
            this.text = text
            setTextColor(Color.BLACK)
            textSize = 16f
            this.layoutParams = layoutParams
            setBackgroundResource(R.drawable.bg_details)
            setPadding(48, 24, 48, 24)
        }
    }

    private fun bindPlan(planIds: List<Long>) {
        viewModel.loadPlans(planIds)
    }

    private fun showDeleteFriendDialog() {
        context?.showAlertDialog(getString(R.string.ask_delete_friend), {
            viewModel.deleteFriend(args.friendId)
        })
    }
}