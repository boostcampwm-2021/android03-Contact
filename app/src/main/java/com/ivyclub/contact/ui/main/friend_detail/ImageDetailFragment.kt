package com.ivyclub.contact.ui.main.friend_detail

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentImageDetailBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.data.ImageManager


class ImageDetailFragment :
    BaseFragment<FragmentImageDetailBinding>(R.layout.fragment_image_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getLong("friendId")?.let {
            initPage(it)
        }
    }

    private fun initPage(friendId: Long) {
        ImageManager.loadProfileImage(friendId)?.let {
            Glide.with(binding.ivProfileImage)
                .load(it)
                .into(binding.ivProfileImage)
        } ?: Glide.with(binding.ivProfileImage)
            .load(R.drawable.photo)
            .into(binding.ivProfileImage)

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 300
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 300
        }
        binding.clImage.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}