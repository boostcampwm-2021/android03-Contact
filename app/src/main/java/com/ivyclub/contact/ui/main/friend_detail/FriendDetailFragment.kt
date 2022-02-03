package com.ivyclub.contact.ui.main.friend_detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentFriendDetailBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.showAlertDialog
import com.ivyclub.data.image.ImageType
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
    }

    private fun setObserver() {
        viewModel.friendData.observe(this, {
            initDetails(it)
        })
        viewModel.finishEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        viewModel.groupName.observe(viewLifecycleOwner) {
            binding.tvGroup.text = it
        }
        viewModel.goPlanDetailsEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(
                FriendDetailFragmentDirections.actionFriendDetailFragmentToPlanDetailsFragment(
                    it
                )
            )
        }
    }

    private fun initButtons(id: Long) {
        with(binding) {
            btnFavorite.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(context, R.anim.star_animation)
                btnFavorite.startAnimation(animation)
                this@FriendDetailFragment.viewModel.setFavorite(id, btnFavorite.isChecked)
            }
            ivMore.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), it)
                popupMenu.menuInflater.inflate(R.menu.menu_friend_detail, popupMenu.menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.item_edit_friend -> {
                            findNavController().navigate(
                                FriendDetailFragmentDirections.actionFriendDetailFragmentToAddEditFriendFragment(
                                    args.friendId
                                )
                            )
                        }
                        R.id.item_delete_friend -> {
                            showDeleteFriendDialog()
                        }
                    }
                    false
                }
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
                val bundle = Bundle().apply {
                    putLong("id", args.friendId)
                    putInt("imageType", ImageType.PROFILE_IMAGE.ordinal)
                }
                findNavController().navigate(
                    R.id.action_friendDetailFragment_to_imageDetailFragment,
                    bundle, // Bundle of args
                    null, // NavOptions
                    extras
                )
            }
        }
    }

    private fun initDetails(friend: FriendData) {
        with(binding) {
            llExtraInfo.removeAllViews()
            for (key in friend.extraInfo.keys) {
                llExtraInfo.addView(getTitle(key))
                llExtraInfo.addView(getContent(friend.extraInfo[key] ?: ""))
            }
            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${friend.phoneNumber}"))
                startActivity(intent)
            }
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
            textSize = 14f
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
            setPadding(32, 24, 32, 24)
        }
    }

    private fun showDeleteFriendDialog() {
        context?.showAlertDialog(getString(R.string.ask_delete_friend), {
            viewModel.deleteFriend(args.friendId)
        })
    }
}