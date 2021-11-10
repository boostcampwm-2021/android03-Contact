package com.ivyclub.contact.ui.main.friend

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemFriendProfileBinding
import com.ivyclub.contact.databinding.ItemGroupDividerBinding
import com.ivyclub.contact.databinding.ItemGroupNameBinding
import com.ivyclub.contact.model.FriendListData
import com.ivyclub.contact.util.FriendListViewType
import com.ivyclub.contact.util.binding
import com.ivyclub.contact.util.setRotateAnimation

class FriendListAdapter(
    private val onGroupClick: (String) -> Unit
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
            FriendListViewType.GROUP_NAME -> (holder as GroupNameViewHolder).bind(currentItem.groupName)
            FriendListViewType.GROUP_DIVIDER -> (holder as GroupDividerViewHolder)
            FriendListViewType.FRIEND -> (holder as FriendViewHolder).bind(currentItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }

    class GroupNameViewHolder(
        private val binding: ItemGroupNameBinding,
        private val onGroupClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var groupName: String
        private var isClicked = false

        init {
            binding.ivFolder.setOnClickListener {
                if (this::groupName.isInitialized) onGroupClick.invoke(groupName)
                else Log.e("FriendListAdapter", "groupName has not been initialized")
                if (isClicked) it.setRotateAnimation(180F, 0F)
                else it.setRotateAnimation(0F, 180F)
                isClicked = !isClicked
            }
        }

        fun bind(groupName: String) {
            binding.groupName = groupName
            this.groupName = groupName
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