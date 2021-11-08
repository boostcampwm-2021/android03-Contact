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
import com.ivyclub.data.model.FriendData

class FriendListAdapter :
    ListAdapter<FriendData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FriendListViewType.GROUP_NAME.ordinal ->
                GroupNameViewHolder(parent.binding(R.layout.item_group_name))
            FriendListViewType.GROUP_DIVIDER.ordinal ->
                GroupDividerViewHolder(parent.binding(R.layout.item_group_divider))
            else ->
                FriendViewHolder(parent.binding(R.layout.item_friend_profile))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        // pk인 phoneNumber가 비어있으면 group 이름을 띄워준다.
        if (currentItem.phoneNumber.isEmpty() && currentItem.groupName.isNotEmpty()) {
            (holder as GroupNameViewHolder).bind(currentItem.groupName)
        } else if (currentItem.phoneNumber.isEmpty() && currentItem.groupName.isEmpty()) {
            (holder as GroupDividerViewHolder)
        } else {
            (holder as FriendViewHolder).bind(currentItem)
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
        private val binding: ItemGroupNameBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groupName: String) {
            binding.groupName = groupName
        }
    }

    class FriendViewHolder(
        private val binding: ItemFriendProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friendItemData: FriendData) {
            binding.data = friendItemData
        }
    }

    class GroupDividerViewHolder(
        private val binding: ItemGroupDividerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendData>() {
            override fun areItemsTheSame(
                oldItem: FriendData,
                newItem: FriendData
            ): Boolean {
                return oldItem.phoneNumber == newItem.phoneNumber
            }

            override fun areContentsTheSame(
                oldItem: FriendData,
                newItem: FriendData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private enum class FriendListViewType {
        GROUP_NAME, FRIEND, GROUP_DIVIDER
    }
}