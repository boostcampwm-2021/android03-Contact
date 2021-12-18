package com.ivyclub.contact.ui.main.add_edit_plan

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemImageAtAddPageBinding
import com.ivyclub.contact.util.binding
import com.ivyclub.contact.util.setCustomBackgroundDrawable

class PhotoAdapter() : ListAdapter<PhotoData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(parent.binding(R.layout.item_image_at_add_page))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoViewHolder).bind(getItem(position))
    }

    class PhotoViewHolder(private val binding: ItemImageAtAddPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photoData: PhotoData) {
            binding.ivImage.setCustomBackgroundDrawable(R.drawable.ic_launcher_background) // todo 실제 이미지 넣는 것으로 수정해야함
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoData>() {
            override fun areItemsTheSame(
                oldItem: PhotoData,
                newItem: PhotoData
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: PhotoData,
                newItem: PhotoData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}