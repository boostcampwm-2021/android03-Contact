package com.ivyclub.contact.ui.main.add_edit_plan

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemImageAtAddPageBinding
import com.ivyclub.contact.util.binding

class PhotoAdapter(
    private val xButtonClickListener: (Int) -> Unit,
) : ListAdapter<Uri, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(
            parent.binding(R.layout.item_image_at_add_page),
            xButtonClickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoViewHolder).bind(getItem(position))
    }

    class PhotoViewHolder(
        private val binding: ItemImageAtAddPageBinding,
        xButtonClickListener: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.circleXImageView.setOnClickListener {
                Log.e("temp", "gooood")
                xButtonClickListener.invoke(adapterPosition)
            }
        }

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