package com.ivyclub.contact.ui.main.friend

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemFriendProfileBinding
import com.ivyclub.contact.util.binding

class FriendListAdapter :
    ListAdapter<FriendItemData, FriendListAdapter.FriendViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(parent.binding(R.layout.item_friend_profile))
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendViewHolder(
        private val binding: ItemFriendProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friendItemData: FriendItemData) {
            binding.data = friendItemData
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendItemData>() {
            override fun areItemsTheSame(
                oldItem: FriendItemData,
                newItem: FriendItemData
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: FriendItemData,
                newItem: FriendItemData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}