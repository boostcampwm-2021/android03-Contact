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
import com.ivyclub.contact.util.setCustomBackgroundColor
import com.ivyclub.contact.util.setRotateAnimation

class FriendListAdapter(
    private val onGroupClick: (String) -> Unit,
    private val onFriendClick: (Long) -> Unit,
    private val onFriendLongClick: (Boolean, Long) -> Unit
) :
    ListAdapter<FriendListData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var longClickedItemCount = 0 // 클릭된 아이템 개수를 확인하는 변수, 0이면 하나도 없는 것

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FriendListViewType.GROUP_NAME.ordinal ->
                GroupNameViewHolder(parent.binding(R.layout.item_group_name), onGroupClick)
            FriendListViewType.GROUP_DIVIDER.ordinal ->
                GroupDividerViewHolder(parent.binding(R.layout.item_group_divider))
            else ->
                FriendViewHolder(
                    parent.binding(R.layout.item_friend_profile),
                    onFriendClick,
                    this::setLongClicked,
                    this::isOneOfItemLongClicked,
                    onFriendLongClick
                )
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

    private fun setLongClicked(isLongClicked: Boolean) {
        if (isLongClicked) longClickedItemCount++
        else longClickedItemCount--
    }

    // 아이템 중 하나라도 클릭 된 것이 있는지 확인하는 함수
    fun isOneOfItemLongClicked(): Boolean {
        return longClickedItemCount != 0
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
        private val binding: ItemFriendProfileBinding,
        private val onFriendClick: (Long) -> Unit,
        private val setLongClicked: (Boolean) -> Unit, // 어댑터에서도 알 수 있도록 해주는 함수
        private val isOneOfItemLongClicked: () -> Boolean, // 다른 아이템 중 하나라도 long clicked 되어 있는지 확인하는 함수
        private val transferClickedIdToViewModel: (Boolean, Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var friendId = -1L
        private var isItemLongClicked = false

        init {
            onFriendClick()
            onFriendLongClick()
        }

        fun bind(friendItemData: FriendListData) {
            binding.data = friendItemData
            this.friendId = friendItemData.id
        }

        private fun onFriendClick() = with(binding.root) {
            setOnClickListener {
                // 롱 클릭 되어 있는 상태에서 누르는 것인지 구분
                if (isOneOfItemLongClicked.invoke()) {
                    isItemLongClicked = !isItemLongClicked // 현재 아이템이 클릭된 아이템으로 명시
                    setLongClicked(isItemLongClicked) // 롱클릭된 아이템 하나 추가
                    setCustomBackgroundColor(if (isItemLongClicked) R.color.blue_100 else R.color.white) // 배경색 변경
                    transferClickedIdToViewModel(isItemLongClicked, friendId)
                } else { // 롱 클릭 하는 중이 아니라면
                    onFriendClick.invoke(friendId) // 해당 친구 리스트로 이동
                }
            }
        }

        private fun onFriendLongClick() {
            binding.root.setOnLongClickListener {
                isItemLongClicked = true
                setLongClicked(true)
                it.setCustomBackgroundColor(R.color.blue_100)
                transferClickedIdToViewModel(true, friendId)
                true // true로 반환하면 click이벤트는 무시하고, longClick 이벤트만 적용하는 것
            }
        }
    }

    class GroupDividerViewHolder(
        binding: ItemGroupDividerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendListData>() {
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