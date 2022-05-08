package com.ivyclub.contact.ui.main.friend_detail

import android.os.Bundle
import android.transition.ChangeBounds
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentImageDetailBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.data.image.ImageManager
import com.ivyclub.data.image.ImageType


class ImageDetailFragment :
    BaseFragment<FragmentImageDetailBinding>(R.layout.fragment_image_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPage(
            arguments?.getLong("id") ?: return,
            arguments?.getInt("imageType") ?: return,
            arguments?.getInt("imageId")
        )
        initCloseButton()
    }

    private fun initPage(id: Long, typeOrdinalNum: Int, imageId: Int?) {
        when (ImageType.values()[typeOrdinalNum]) {
            ImageType.PLAN_IMAGE -> {
                loadPlanImage(id, imageId ?: return)
            }
            ImageType.PROFILE_IMAGE -> {
                loadProfileImage(id)
                sharedElementEnterTransition = ChangeBounds().apply {
                    duration = 300
                }
                sharedElementReturnTransition = ChangeBounds().apply {
                    duration = 300
                }
            }
        }
    }

    private fun loadPlanImage(planId: Long, imageId: Int) {
        Log.e("@@@plan,image", ".$planId, $imageId")
        ImageManager.loadPlanBitmap(planId, imageId)?.let { imageBitmap ->
            Glide.with(binding.ivProfileImage)
                .load(imageBitmap)
                .into(binding.ivProfileImage)
        } ?: Glide.with(binding.ivProfileImage)
            .load(R.drawable.photo)
            .into(binding.ivProfileImage)
    }

    private fun loadProfileImage(id: Long) {
        ImageManager.loadProfileImage(id)?.let {
            Glide.with(binding.ivProfileImage)
                .load(it)
                .into(binding.ivProfileImage)
        } ?: Glide.with(binding.ivProfileImage)
            .load(R.drawable.photo)
            .into(binding.ivProfileImage)
    }

    private fun initCloseButton() {
        binding.ivBtnClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}