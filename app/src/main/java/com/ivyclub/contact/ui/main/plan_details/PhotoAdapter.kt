package com.ivyclub.contact.ui.main.plan_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemPlanPhotoBinding

class PhotoAdapter(private val photoList: List<String>) :
    RecyclerView.Adapter<PhotoAdapter.PagerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPlanPhotoBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_plan_photo, parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size

    inner class PagerViewHolder(private val binding: ItemPlanPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageName: String) {
            val imageString = "${binding.ivPhoto.context.cacheDir}/$imageName.jpg"
            Glide.with(binding.ivPhoto)
                .load(imageString)
                .into(binding.ivPhoto)
        }
    }
}