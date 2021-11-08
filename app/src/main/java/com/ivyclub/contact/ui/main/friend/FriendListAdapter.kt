package com.ivyclub.contact.ui.main.friend

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemFriendProfileBinding
import com.ivyclub.contact.databinding.ItemGroupDividerBinding
import com.ivyclub.contact.databinding.ItemGroupNameBinding
import com.ivyclub.contact.util.binding

class FriendListAdapter(
    private val onGroupClick: () -> Unit
) :
    ListAdapter<FriendListData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FriendListViewType.GROUP_NAME.ordinal ->
                GroupNameViewHolder(parent.binding(R.layout.item_group_name), onGroupClick)
            FriendListViewType.GROUP_DIVIDER.ordinal ->
                GroupDividerViewHolder(parent.binding(R.layout.item_group_divider))
            else ->
                FriendViewHolder(parent.binding(R.layout.item_friend_profile))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (currentItem.viewType) {
            FriendListViewType.GROUP_NAME -> {
                (holder as GroupNameViewHolder).bind(currentItem.groupName)
            }
            FriendListViewType.GROUP_DIVIDER -> {
                (holder as GroupDividerViewHolder)
            }
            FriendListViewType.FRIEND -> {
                (holder as FriendViewHolder).bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return if (currentItem.phoneNumber.isEmpty() && currentItem.groupName.isNotEmpty()) {
            FriendListViewType.GROUP_NAME.ordinal
        } else if (currentItem.phoneNumber.isEmpty() && currentItem.groupName.isEmpty()) {
            FriendListViewType.GROUP_DIVIDER.ordinal
        } else {
            FriendListViewType.FRIEND.ordinal
        }
    }

    // todo 그룹 접기 리스너 추가
    class GroupNameViewHolder(
        private val binding: ItemGroupNameBinding,
        private val onGroupClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivFolder.setOnClickListener { onGroupClick }
        }

        fun bind(groupName: String) {
            binding.groupName = groupName
        }
    }

    class FriendViewHolder(
        private val binding: ItemFriendProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friendItemData: FriendListData) {
            binding.data = friendItemData
        }
    }

    class GroupDividerViewHolder(
        binding: ItemGroupDividerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendListData>() {
            override fun areItemsTheSame(
                oldItem: FriendListData,
                newItem: FriendListData
            ): Boolean {
                return oldItem.phoneNumber == newItem.phoneNumber
            }

            override fun areContentsTheSame(
                oldItem: FriendListData,
                newItem: FriendListData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}