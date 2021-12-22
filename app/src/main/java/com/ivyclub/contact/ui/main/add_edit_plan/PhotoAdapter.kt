package com.ivyclub.contact.ui.main.add_edit_plan

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemImageAtAddPageBinding
import com.ivyclub.contact.util.binding

class PhotoAdapter : ListAdapter<Uri, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(parent.binding(R.layout.item_image_at_add_page))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoViewHolder).bind(getItem(position))
    }

    class PhotoViewHolder(private val binding: ItemImageAtAddPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photoUri: Uri) {
            binding.ivImage.setImageURI(photoUri)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(
                oldItem: Uri,
                newItem: Uri
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Uri,
                newItem: Uri
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}