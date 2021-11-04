package com.ivyclub.contact.ui.main.friend

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemFriendProfileBinding
import com.ivyclub.contact.util.binding
import com.ivyclub.data.model.PersonData

class FriendListAdapter :
    ListAdapter<PersonData, FriendListAdapter.FriendViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(parent.binding(R.layout.item_friend_profile))
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendViewHolder(
        private val binding: ItemFriendProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friendItemData: PersonData) {
            binding.data = friendItemData
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PersonData>() {
            override fun areItemsTheSame(
                oldItem: PersonData,
                newItem: PersonData
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: PersonData,
                newItem: PersonData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}